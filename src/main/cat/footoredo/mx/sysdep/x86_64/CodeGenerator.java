package cat.footoredo.mx.sysdep.x86_64;

import cat.footoredo.mx.asm.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.ir.IR;
import cat.footoredo.mx.ir.IRVisitor;
import cat.footoredo.mx.ir.Integer;
import cat.footoredo.mx.ir.Statement;
import cat.footoredo.mx.utils.AsmUtils;
import cat.footoredo.mx.utils.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CodeGenerator implements cat.footoredo.mx.sysdep.CodeGenerator, IRVisitor<Void, Void> {
    private final Type naturalType;

    public CodeGenerator (Type naturalType) {
        this.naturalType = naturalType;
    }

    public AssemblyCode generate (IR ir) {
        locateSymbols(ir);
        return generateAssemblyCode(ir);
    }

    private static final String LABEL_SYMBOL_BASE = ".L";
    private static final String CONST_SYMBOL_BASE = ".LC";

    private void locateSymbols (IR ir) {
        SymbolTable constSymbols = new SymbolTable(CONST_SYMBOL_BASE);
        for (ConstantEntry entry: ir.getConstantTable().entries()) {
            locateStringLiteral (entry, constSymbols);
        }
        for (Variable variable: ir.getVariables()) {
            locateVariable (variable);
        }
        for (Function function: ir.getAllFunctions()) {
            locateFunction (function);
        }
    }

    private void locateStringLiteral(ConstantEntry entry, SymbolTable symbolTable) {
        entry.setSymbol(symbolTable.newSymbol());
        entry.setMemoryReference(memory(entry.getSymbol()));
        entry.setAddress(immediate(entry.getSymbol()));
    }

    private void locateVariable (Entity entity) {
        Symbol sym = symbol (entity.getSymbolString());
        entity.setMemoryReference(memory(sym));
        entity.setAddress(immediate(sym));
    }

    private void locateFunction (Function function) {
        function.setCallingSymbol(symbol (function.getSymbolString()));
        locateVariable(function);
    }

    private Symbol symbol(String sym) {
        return new NamedSymbol(sym);
    }

    private AssemblyCode generateAssemblyCode(IR ir) {
        AssemblyCode file = newAssemblyCode();
        generateDataSection (file, ir.getGlobalVariables());
        generateTextSection (file, ir.getConstantTable());
        generateTextSection (file, ir.getDefinedFunctions());
        generateCommonSymbols (file, ir.getCommonSymbols());
        return file;
    }

    private AssemblyCode newAssemblyCode() {
        return new AssemblyCode(naturalType, STACK_WORD_SIZE, new SymbolTable(LABEL_SYMBOL_BASE));
    }

    private void generateDataSection(AssemblyCode file, List<Variable> globalVariables) {
        file._data();
        for (Variable variable: globalVariables) {
            Symbol sym = symbol(variable.getSymbolString());
            file.label (sym);
            file.d (variable.size(), ((Integer)variable.getIR()).getValue());
        }
    }

    private void generateTextSection(AssemblyCode file, ConstantTable constants) {
        file._text();
        for (ConstantEntry constant: constants) {
            file.label (constant.getSymbol());
            file.d (constant.getValue());
        }
    }

    private void generateCommonSymbols(AssemblyCode file, List<Variable> commomSymbols) {
        file._bss();
        for (Variable variable: commomSymbols) {
            Symbol sym = symbol(variable.getSymbolString());
            file.label(sym);
            file.res(variable.size());
        }
    }

    private void generateTextSection (AssemblyCode file, List<DefinedFunction> definedFunctions) {
        file._text();
        for (DefinedFunction function: definedFunctions) {
            Symbol sym = symbol(function.getName());
            file.label(sym);
            compileFunctionBody (file, function);
        }
    }

    static final private long STACK_WORD_SIZE = 8;

    private long alignStack(long size) {
        return AsmUtils.align(size, STACK_WORD_SIZE);
    }

    class StackFrameInfo {
        List <Register> savedRegs;
        long lvarSize;
        long tempSize;

        long savedRegsSize() { return savedRegs.size() * STACK_WORD_SIZE; }
        long lvarOffset() { return savedRegsSize(); }
        long tempOffset() { return savedRegsSize() + lvarSize; }
        long frameSize() { return savedRegsSize() + lvarSize + tempSize; }
    }

    private void compileFunctionBody (AssemblyCode file, DefinedFunction function) {
        StackFrameInfo frame = new StackFrameInfo();
        locateParameters (function.getParameters());
        frame.lvarSize = locateLocalVariables(function.getLvarScope());

        AssemblyCode body = compileStatements(function);
        frame.savedRegs = usedCalleeSaveRegisters(body);
        frame.tempSize = body.getVirtualStack().getMaxSize();

        fixLocalVariableOffsets (function.getLvarScope(), frame.lvarOffset());
        fixTempVariableOffsets (body, frame.tempOffset());

        generateFunctionBody (file, body, frame);
    }

    class MemInfo {
        MemoryReference mem;
        String name;

        MemInfo (MemoryReference mem, String name) {
            this.mem = mem;
            this.name = name;
        }
    }

    private void printStackFrameLayout (AssemblyCode file, StackFrameInfo frame, List<Variable> lvars) {
        List<MemInfo> vars = new ArrayList<>();
        for (Variable var: lvars) {
            vars.add (new MemInfo (var.getMemoryReference()), var.getName());
        }
        vars.add (new MemInfo(memory(0, bp()), "saved rbp"));
        vars.add (new MemInfo(memory(8, bp()), "return address"));
        if (frame.savedRegsSize() > 0) {
            vars.add(new MemInfo(memory (-frame.savedRegsSize(), bp()),
                    "saved callee-saved registers (" + frame.savedRegsSize() + " bytes)"));
        }
        if (frame.tempSize > 0) {
            vars.add(new MemInfo(memory (-frame.frameSize(), bp()),
                    "tmp variables (" + frame.tempSize + " bytes)"));
        }
        Collections.sort(vars, new Comparator<MemInfo>() {
            @Override
            public int compare(MemInfo memInfo, MemInfo t1) {
                return memInfo.mem.compareTo(t1.mem);
            }
        });
        file.comment("---- Stack Frame Layout -----------");
        for (MemInfo info : vars) {
            file.comment(info.mem.toString() + ": " + info.name);
        }
        file.comment("-----------------------------------");
    }

    private AssemblyCode as;
    private Label epilogueLabel;

    private AssemblyCode compileStatements (DefinedFunction function) {
        as = newAssemblyCode();
        epilogueLabel = new Label();
        for (Statement statement: function.getIR()) {
            compileStatement (statement);
        }
        as.label(epilogueLabel);
        return as;
    }

    // does NOT include BP
    private List<Register> usedCalleeSaveRegisters (AssemblyCode body) {
        List<Register> result = new ArrayList<>();
        for (Register register: calleeSaveRegisters()) {
            if (body.uses(register)) {
                result.add(register);
            }
        }
        result.remove (bp());
        return result;
    }

    static final long[] CALLEE_SAVE_REGISTERS = {
            RegisterClass.BP.ordinal(), RegisterClass.BX.ordinal(),
            12, 13, 14, 15
    };

    private List<Register> calleeSaveRegistersCache = null;

    private List<Register> calleeSaveRegisters() {
        if (calleeSaveRegistersCache == null) {
            List<Register> registers = new ArrayList<>();
            for (long c: CALLEE_SAVE_REGISTERS) {
                registers.add (new Register(c, naturalType));
            }
            calleeSaveRegistersCache = registers;
        }
        return calleeSaveRegistersCache;
    }

    private void generateFunctionBody(AssemblyCode file,
                                      AssemblyCode body,
                                      StackFrameInfo frame) {
        file.virtualStack.reset();
        prologue (file, frame.savedRegs, frame.frameSize());
        file.addAll(body.getAssemblies());
        epilogue (file, frame.savedRegs);
        file.virtualStack.fixOffset(0);
    }

    private void prologue (AssemblyCode file,
                           List<Register> savedRegisters,
                           long frameSize) {
        file.push (bp());
        file.mov (bp(), sp());
        for (Register register: savedRegisters) {
            file.virtualPush(register);
        }
        extendStack(file, frameSize);
    }

    private void epilogue (AssemblyCode file,
                           List<Register> savedRegisters) {
        for (Register register: ListUtils.reverse(savedRegisters)) {
            file.virtualPop(register);
        }
        file.mov (sp(), bp());
        file.pop (bp());
        file.ret ();
    }
}
