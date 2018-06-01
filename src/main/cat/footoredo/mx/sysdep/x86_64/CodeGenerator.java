package cat.footoredo.mx.sysdep.x86_64;

import cat.footoredo.mx.asm.*;
import cat.footoredo.mx.cfg.*;
import cat.footoredo.mx.cfg.Instruction;
import cat.footoredo.mx.cfg.Operand;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.ir.*;
import cat.footoredo.mx.ir.Integer;
import cat.footoredo.mx.ir.String;
import cat.footoredo.mx.utils.AsmUtils;
import cat.footoredo.mx.utils.ListUtils;
import cat.footoredo.mx.utils.StringUtils;

import java.util.*;

public class CodeGenerator implements cat.footoredo.mx.sysdep.CodeGenerator, CFGVisitor {
    private final Type naturalType;

    public CodeGenerator (Type naturalType) {
        this.naturalType = naturalType;
    }

    private Set<Label> compiledLabels;
    private CFG cfg;
    public AssemblyCode generate (IR ir, CFG cfg) {
        this.cfg = cfg;
        compiledLabels = new HashSet<>();
        locateSymbols(ir);
        return generateAssemblyCode(ir).peepholeOptimize();
    }

    private static final java.lang.String LABEL_SYMBOL_BASE = "_QAQ_L";
    private static final java.lang.String CONST_SYMBOL_BASE = "_QAQ_LC";

    private void locateSymbols (IR ir) {
        SymbolTable constSymbols = new SymbolTable(CONST_SYMBOL_BASE);
        for (ConstantEntry entry: ir.getConstantTable().entries()) {
            locateStringLiteral (entry, constSymbols);
        }
        /*for (Variable variable: ir.getVariables()) {
            locateVariable (variable);
        }*/
        for (Variable variable: ir.getScope().getVariables()) {
            locateGlobalVariable (variable);
        }
        for (Function function: ir.getScope().getAllFunctions()) {
            locateFunction (function);
        }
    }

    private void locateStringLiteral(ConstantEntry entry, SymbolTable symbolTable) {
        entry.setSymbol(symbolTable.newSymbol());
        entry.setMemoryReference(memory(Type.INT64, entry.getSymbol()));
        entry.setAddress(immediate(entry.getSymbol()));
    }

    private void locateGlobalVariable (Variable variable) {
        Symbol sym = symbol (variable.getSymbolString());
        variable.setMemory(memory(Type.get(variable.size()), sym));
    }

    private void locateFunction (Function function) {
        if (function.getCallingSymbol() == null) {
            /*if (function instanceof DefinedFunction)
                throw new Error ("wtf");*/
            function.setCallingSymbol(symbol (function.getSymbolString()));
        }
        // locateVariable(function);
    }

    private Symbol symbol(java.lang.String sym) {
        return new NamedSymbol(sym);
    }

    private AssemblyCode generateAssemblyCode(IR ir) {
        AssemblyCode file = newAssemblyCode();
        file._global("main");
        generateExterns (file);
        generateDataSection (file, ir.getScope().getStaticVariables());
        generateTextSection (file, ir.getScope().getAllDefinedFunctions());
        // generateGlobalBuiltinFunctions (file, ir.getBuiltinFunctions());
        generateRodataSection (file, ir.getConstantTable());
        generateCommonSymbols (file, ir.getScope().getNonstaticVariables());
        return file;
    }

    private void generateExterns (AssemblyCode file) {
        file._extern("malloc, _Znam, _IO_getc, _IO_putc, stdin, stdout");
        file._extern("putchar, puts, scanf, sprintf");
        file._extern("strcmp, strcpy, strlen, strncpy");
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

    private void generateRodataSection(AssemblyCode file, ConstantTable constants) {
        file._rodata();
        for (ConstantEntry constant: constants) {
            file.label (constant.getSymbol());
            file.d (StringUtils.escape(constant.getValue()));
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
            // System.out.println(function.getName());
            Symbol sym = symbol(function.getSymbolString());
            file.label(sym);
            compileFunctionBody (file, function);
        }
    }

    static final private long STACK_WORD_SIZE = 8;

    private long alignStack(long size) {
        return AsmUtils.align(size, STACK_WORD_SIZE * 2);
    }

    private long stackSizeFromPosition (long position) {
        return position * STACK_WORD_SIZE;
    }

    class StackFrameInfo {
        List <Register> savedRegs;
        List <Register> toSaveRegs;
        long lvarSize;
        long tempSize;

        long savedRegsSize() { return (savedRegs.size() + toSaveRegs.size()) * STACK_WORD_SIZE; }
        long lvarOffset() { return savedRegsSize(); }
        long tempOffset() { return savedRegsSize() + lvarSize; }
        long frameSize() { return savedRegsSize() + lvarSize + tempSize; }
    }

    private Set<Register> toSaveRegisters, allToSaveRegisters;

    private void compileFunctionBody (AssemblyCode file, DefinedFunction function) {
        StackFrameInfo frame = new StackFrameInfo();
        locateParameters (function.getParameters());
        // System.out.println (function.getName());
        frame.lvarSize = locateLocalVariables(function.getScope());

        allToSaveRegisters = new HashSet<>();
        allToSaveRegisters.addAll(callerSaveRegisters());
        toSaveRegisters = new HashSet<>();
        visitedBasicBlock = new HashSet<>();

        for (Variable variable: function.getScope().getAllVariables()) {
            if (variable.isRegister()) {
                Register register = variable.getRegister();
                if (allToSaveRegisters.contains(register)) {
                    toSaveRegisters.add (register);
                }
            }
        }

        toSaveRegisters.add (new Register(RegisterClass.DI));
        // toSaveRegisters.add (new Register(RegisterClass.SI));

        AssemblyCode body = compileStatements(function);
        frame.savedRegs = usedCalleeSaveRegisters(body);
        frame.toSaveRegs = new ArrayList<>();
        frame.toSaveRegs.addAll (toSaveRegisters);
        frame.tempSize = body.getVirtualStack().getMaxSize();

        fixLocalVariableOffsets (function.getScope(), frame.lvarOffset());
        fixTempVariableOffsets (body, frame.tempOffset());

        generateFunctionBody (file, body, frame, function.getParameters());
    }

    class MemInfo {
        MemoryReference mem;
        java.lang.String name;

        MemInfo (MemoryReference mem, java.lang.String name) {
            this.mem = mem;
            this.name = name;
        }
    }

    /*private void printStackFrameLayout (AssemblyCode file, StackFrameInfo frame, List<Variable> lvars) {
        List<MemInfo> vars = new ArrayList<>();
        for (Variable var: lvars) {
            vars.add (new MemInfo (var.getMemoryReference(), var.getName()));
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
    }*/

    private AssemblyCode as;
    private Label epilogueLabel;

    private Set<BasicBlock> compiledBasicBlocks;
    private void compileBasicBlock (BasicBlock block) {
        if (block == null)
            throw new Error("fasas");
        compiledBasicBlocks.add (block);
        as.label(block.getLabel());
        // compiledLabels.add (block.getLabel());
        for (Instruction instruction: block.getInstructions()) {
            compileInstruction (instruction);
        }
        if (block.getJumpInst() != null)
            block.getJumpInst().accept (this);
        for (BasicBlock output: block.getOutputs()) {
            if (!compiledBasicBlocks.contains (output)) {
                compileBasicBlock(output);
            }
        }
        // compileJump(block.getJumpInst());
    }

    private void compileInstruction (Instruction instruction) {
        instruction.accept (this);
    }

    private void compileJump (JumpInst jumpInst) {
        if (jumpInst != null) {
            jumpInst.accept(this);
            for (Label label : jumpInst.getOutputs()) {
                // System.err.println(label.hashCode());
                if (!compiledLabels.contains(label)) {
                    compileBasicBlock(cfg.get(label));
                }
            }
        }
    }

    private AssemblyCode compileStatements (DefinedFunction function) {
        as = newAssemblyCode();
        for (Parameter par: function.getParameters()) if (par.isUsed()) {
            // System.out.println (par.getName() + " " + par.getSpace() + " " + par.getParameterSpace());
            compileAssign (as, Type.get(par.size()), Type.get(par.size()), par.getSpace(), par.getParameterSpace());
        }
        epilogueLabel = new Label();
        // System.out.println (function.getSymbolString());
        // System.out.println ("qurying " + (new Label(function.getCallingSymbol())).hashCode());
        compiledBasicBlocks = new HashSet<>();
        compileBasicBlock(function.getStartBasicBlock());
        as.label(epilogueLabel);
        return as;
    }

    // does NOT include BP
    private List<Register> usedCalleeSaveRegisters (AssemblyCode body) {
        //System.out.println("jere");
        List<Register> result = new ArrayList<>();
        for (Register register: calleeSaveRegisters()) {
            //System.out.println(register.getNumber());
            //if (register.getNumber() == 7)
            //    System.out.println (body.used(register));
            if (body.used(register)) {
                // System.out.println (register.getNumber());
                result.add(register);
            }
        }
        result.remove (bp());
        return result;
    }

    private Set<BasicBlock> visitedBasicBlock;


    static final long[] CALLEE_SAVE_REGISTERS = {
            RegisterClass.BP.getValue(), RegisterClass.BX.getValue(),
            12, 13, 14, 15/*,
            10, 11, 8, 9*/
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

    static final long[] CALLER_SAVE_REGISTERS = {
            RegisterClass.SI.getValue(), RegisterClass.DI.getValue(),
            10, 11, 8, 9
    };

    private List<Register> callerSaveRegistersCache = null;

    private List<Register> callerSaveRegisters() {
        if (callerSaveRegistersCache == null) {
            List<Register> registers = new ArrayList<>();
            for (long c: CALLER_SAVE_REGISTERS) {
                registers.add (new Register(c, naturalType));
            }
            callerSaveRegistersCache = registers;
        }
        return callerSaveRegistersCache;
    }

    private void compileAssign (AssemblyCode as, Type leftType, Type rightType, cat.footoredo.mx.asm.Operand a, cat.footoredo.mx.asm.Operand b) {
        if (b.isMemoryReference()) {
            as.mov (ax(rightType), b);
            b = ax(rightType);
        }
        if (b.isConstant() || leftType == rightType) {
            // System.out.println (a.getClass() + " " + b.getClass());
            // System.out.println (a + " " + b);
            as.mov (a, b);
        }
        else {
            if (leftType.size() < rightType.size()) {
                as.mov(a, ((Register) b).forType(leftType));
            }
            else {
                if (a.isMemoryReference()) {
                    as.movzx (ax(leftType), b);
                    as.mov (a, ax(leftType));
                }
                else {
                    as.movzx((Register) a, b);
                }
            }
        }
    }

    private void compileAssign (AssemblyCode as, Operand a, Operand b) {
        compileAssign(as, a.getType(), b.getType(), a.toASMOperand(), b.toASMOperand());
    }

    private void compileAssign (Operand a, Operand b) {
        compileAssign(as, a, b);
    }

    private void generateFunctionBody(AssemblyCode file,
                                      AssemblyCode body,
                                      StackFrameInfo frame,
                                      List<Parameter> parameters) {
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
        file.leave ();
        file.ret ();
    }

    static final long[] PARAMETER_REGISTERS = {/*
            RegisterClass.DI.getValue(), RegisterClass.SI.getValue(),
            RegisterClass.DX.getValue(), RegisterClass.CX.getValue(), 8, 9*/
    };

    // static final long [] PARAMETER_REGISTERS = {};

    static final private long PARAMETER_START_POSITION = 2;

    private void locateParameters (List<Parameter> parameters) {
        for (int i = 0; i < PARAMETER_REGISTERS.length && i < parameters.size(); ++ i) {
            Parameter var = parameters.get(i);
            var.setParameterSpace(new Register(PARAMETER_REGISTERS[i], Type.get(var.size())));
        }

        long currentPosition = PARAMETER_START_POSITION;
        for (int i = PARAMETER_REGISTERS.length; i < parameters.size(); ++ i) {
            Parameter var = parameters.get(i);
            var.setParameterSpace(memory(naturalType, stackSizeFromPosition(currentPosition), bp()));
            currentPosition ++;
        }
    }

    private long locateLocalVariables (LocalScope scope) {
        return locateLocalVariables(scope, 0);
    }

    private long locateLocalVariables (LocalScope scope, long parentStackLength) {
        long length = parentStackLength;
        for (Variable variable: scope.getVariables()) if (!variable.isRegister() && variable.isUsed()) {
            // System.out.println(variable.getName());
            length = alignStack(length + variable.size());
            variable.setMemory(relocatableMemory(Type.get(variable.size()), -length, bp()));
        }

        long maxLength = length;
        for (LocalScope s: scope.getChildren()) {
            long childLength = locateLocalVariables(s, length);
            if (childLength > maxLength)
                maxLength = childLength;
        }
        return maxLength;
    }

    private void fixLocalVariableOffsets(LocalScope scope, long length) {
        for (Variable variable: scope.getAllVariables()) if (!variable.isRegister() && variable.isUsed()) {
            variable.getMemory().fixOffset(-length);
        }
    }

    private void fixTempVariableOffsets (AssemblyCode asm, long length) {
        asm.virtualStack.fixOffset(-length);
    }

    private void extendStack(AssemblyCode file, long length) {
        if (length > 0) {
            file.sub (sp(), immediate(length));
        }
    }

    private void rewindStack (AssemblyCode file, long length) {
        if (length > 0) {
            file.add (sp(), immediate(length));
        }
    }

    @Override
    public void visit(AssignInst s) {
        // System.err.println (s.getLeft().getClass() + " " + s.getRight().getClass());
        if (s.isDeref()) {
            // System.out.print("asd");
            compileAssign(as, s.getLeft().getType(), s.getRight().getType(), new IndirectMemoryReference(s.getLeft().getType(), 0, cx()), s.getRight().toASMOperand());
        }
        else {
            compileAssign(s.getLeft(), s.getRight());
        }
    }

    @Override
    public void visit(ConditionalJumpInst s) {
        Type t = s.getCondition().getType();
        as.mov(ax(t), s.getCondition().toASMOperand());
        as.test (ax(t), ax(t));
        as.j("nz", s.getTrueTarget());
        as.jmp(s.getFalseTarget());
    }

    @Override
    public void visit(UnconditionalJumpInst s) {
        as.jmp(s.getTarget());
    }

    @Override
    public void visit(ReturnInst s) {
        if (s.hasValue()) {
            // System.out.println ("asd");
            as.mov (ax(s.getValue().getType()), s.getValue().toASMOperand());
        }
    }

    @Override
    public void visit(ULTIMATERETURNINST inst) {
        as.jmp(epilogueLabel);
    }

    @Override
    public void visit(UnaryInst s) {
        cat.footoredo.mx.asm.Operand result = s.getResult().toASMOperand();
        compileAssign(s.getResult(), s.getOperand());

        switch (s.getOp()) {
            case UMINUS:
                as.neg(result);
                break;
            case BIT_NOT:
                as.not(result);
                break;
            case NOT:
                Type t = s.getResult().getType();
                as.mov (ax(t), result);
                as.test (ax(t), ax(t));
                as.set ("e", al());
                if (t.size() > 1) {
                    as.movzx(ax(t), al());
                }
                as.mov (result, ax(t));
        }
    }

    @Override
    public void visit(BinaryInst s) {
        Op op = s.getOp();
        Type t = s.getResult().getType();
        cat.footoredo.mx.asm.Operand resultOperand = s.getResult().toASMOperand();
        cat.footoredo.mx.asm.Operand leftOperand = s.getLeft().toASMOperand();
        cat.footoredo.mx.asm.Operand rightOperand = s.getRight().toASMOperand();
        cat.footoredo.mx.asm.Operand tmpOperand = ax(s.getLeft().getType());
        as.mov (tmpOperand, leftOperand);/*
        if (s.getResult().isMemory() && s.getRight().isMemory()) {
            as.mov (cx(t), s.getRight().toASMOperand());
            rightOperand = cx(t);
        }*/
        compileBinaryOp(t, s.getLeft().getType(), op, tmpOperand, rightOperand);
        as.mov (resultOperand, ax(t));
    }

    private boolean requireRegisterOperand (Op op) {
        switch (op) {
            case S_DIV:
            case U_DIV:
            case S_MOD:
            case U_MOD:
            case BIT_LSHIFT:
            case BIT_RSHIFT:
            case ARITH_RSHIFT:
                return true;

                default:
                    return false;
        }
    }

    private void compileBinaryOp (Type resultType, Type operandType, Op op, cat.footoredo.mx.asm.Operand left, cat.footoredo.mx.asm.Operand right) {
        Type t = operandType;
        switch (op) {
            case ADD:
                if (left.isConstant()) {
                    throw new Error ("asdas");
                }
                as.add(left, right);
                break;
            case SUB:
                as.sub(left, right);
                break;
            case MUL:
                if (!left.isRegister()) {
                    as.mov (ax(resultType), left);
                    as.imul(ax(resultType), right);
                    as.mov (left, ax(resultType));
                }
                else {
                    as.imul(left, right);
                }
                break;
            case S_DIV:
            case S_MOD:
                as.c(t);
                as.mov (cx(t), right);
                as.idiv(right);
                if (op == Op.S_MOD) {
                    as.mov(left, dx(t));
                }
                else {
                    as.mov (left, ax(t));
                }
                break;
            case U_DIV:
            case U_MOD:
                as.mov(dx(t), immediate(0));
                as.mov (cx(t), right);
                as.div(right);
                if (op == Op.U_MOD) {
                    as.mov (left, dx(t));
                }
                else {
                    as.mov (left, ax(t));
                }
                break;
            case BIT_AND:
                as.and (left, right);
                break;
            case BIT_OR:
                as.or (left, right);
                break;
            case LOGIC_AND:
                as.and (left, right);
                break;
            case LOGIC_OR:
                as.or (left, right);
                break;
            case BIT_XOR:
                as.xor (left, right);
                break;
            case BIT_LSHIFT:
                as.mov (cx(operandType), right);
                as.shl (left, cl());
                break;
            case BIT_RSHIFT:
                as.mov (cx(operandType), right);
                as.shr (left, cl());
                break;
            case ARITH_RSHIFT:
                as.mov (cx(operandType), right);
                as.sar (left, cl());
                break;
                default:
                    as.cmp (left, right);
                    switch (op) {
                        case EQ:    as.set ("e",  al()); break;
                        case NEQ:   as.set ("ne", al()); break;
                        case S_GT:  as.set ("g",  al()); break;
                        case S_GTEQ:as.set ("ge", al()); break;
                        case S_LT:  as.set ("l",  al()); break;
                        case S_LTEQ:as.set ("le", al()); break;
                        case U_GT:  as.set ("a",  al()); break;
                        case U_GTEQ:as.set ("ae", al()); break;
                        case U_LT:  as.set ("b",  al()); break;
                        case U_LTEQ:as.set ("be", al()); break;
                        default:
                            throw new Error ("unknown binary operator: " + op);
                    }
                    if (t.size() > 1)
                        as.movzx (ax(t), al());
                    as.mov (left, ax(t));
        }
    }

    @Override
    public void visit(CallInst s) {
        List<Register> saving = new ArrayList<>(toSaveRegisters);
        // System.out.println (saving.size() + " " + s);
        for (Register register: saving) {
            as.virtualPush(register);
        }
        for (int i = 0; i < s.getArgc() && i < PARAMETER_REGISTERS.length; ++ i) {
            Operand operand = s.getArg(i);
            Type t = operand.getType();
            as.mov (new Register(PARAMETER_REGISTERS[i], t), operand.toASMOperand());
        }
        for (int i = s.getArgc() - 1; i >= PARAMETER_REGISTERS.length; -- i) {
            Operand operand = s.getArg(i);
            Type t = operand.getType();
            as.mov (ax(t), operand.toASMOperand());
            as.push (ax(t));
            // as.mov (s.getFunction().getParameter(i).getMemoryReference(), ax());
        }
        // System.out.println (call.)
        //System.out.println (s.getFunction().getName());
        as.call(s.getFunction().getCallingSymbol());
        /*if (s.getResult().toASMOperand() == null) {
            System.out.println (s.getResult().getVariable().getName() + s.getResult().getVariable().isUsed());
        }*/
        saving = new ArrayList<>(toSaveRegisters);
        for (Register register: ListUtils.reverse(saving)) {
            as.virtualPop(register);
        }
        if (s.hasResult()) {
            as.mov(s.getResult().toASMOperand(), ax(s.getResult().getType()));
        }
    }

    /*@Override
    public Void visit(Address s) {
        loadAddress(ax(), s.getEntity());
        return null;
    }*/

    @Override
    public void visit(DereferenceInst s) {
        as.mov(cx(), s.getAddress().toASMOperand());
        load(ax(s.getResult().getType()), memory(s.getResult().getType(), cx()));
        // System.out.println (s.getResult().getType());
        as.mov(s.getResult().toASMOperand(), ax(s.getResult().getType()));
    }

    @Override
    public void visit(MallocInst s) {
        List<Register> saving = new ArrayList<>(toSaveRegisters);
        // System.out.println (saving.size() + " " + s);
        for (Register register: saving) {
            as.virtualPush(register);
        }
        /* as.mov (ax(s.getLength().getType()), s.getLength().toASMOperand());
        as.push (ax(s.getLength().getType()));*/
        as.mov (new Register(RegisterClass.DI, s.getLength().getType()), s.getLength().toASMOperand());
        as.call(new NamedSymbol("malloc"));
        for (Register register: ListUtils.reverse(saving)) {
            as.virtualPop(register);
        }
        as.mov (s.getResult().toASMOperand(), ax());
    }

    private IndirectMemoryReference relocatableMemory(Type type, long offset, Register base) {
        return IndirectMemoryReference.relocatable(type, offset, base);
    }

    private Register ax () { return ax (naturalType); }
    private Register al () { return ax (Type.INT8); }
    private Register cx () { return cx (naturalType); }
    private Register cl () { return cx (Type.INT8); }
    private Register dx () { return dx (naturalType); }
    private Register bx () { return bx (naturalType); }
    private Register sp () { return sp (naturalType); }
    private Register bp () { return bp (naturalType); }
    private Register si () { return si (naturalType); }
    private Register di () { return di (naturalType); }
    private Register r (int index) { return r (index, naturalType); }

    private Register ax (Type type) {
        return new Register(RegisterClass.AX, type);
    }

    private Register cx (Type type) {
        return new Register(RegisterClass.CX, type);
    }

    private Register dx (Type type) {
        return new Register(RegisterClass.DX, type);
    }

    private Register bx (Type type) {
        return new Register(RegisterClass.BX, type);
    }

    private Register sp (Type type) {
        return new Register(RegisterClass.SP, type);
    }

    private Register bp (Type type) {
        return new Register(RegisterClass.BP, type);
    }

    private Register si (Type type) {
        return new Register(RegisterClass.SI, type);
    }

    private Register di (Type type) {
        return new Register(RegisterClass.DI, type);
    }

    private Register r (int index, Type type) {
        return new Register(index, type);
    }

    private DirectMemoryReference memory (Type type, Symbol symbol) {
        return new DirectMemoryReference(type, symbol);
    }

    private IndirectMemoryReference memory (Type type, Register base) {
        return new IndirectMemoryReference(type, 0, base);
    }

    private IndirectMemoryReference memory (Type type, long offset, Register base) {
        return new IndirectMemoryReference(type, offset, base);
    }

    private ImmediateValue immediate (long value) {
        return new ImmediateValue(value);
    }

    private ImmediateValue immediate (Symbol symbol) {
        return new ImmediateValue(symbol);
    }

    private ImmediateValue immediate (Literal literal) {
        return new ImmediateValue(literal);
    }

    private void load (Register register, MemoryReference memory) {
        as.mov (register, memory);
    }

    private void store (MemoryReference memory, Register register) {
        as.mov (memory, register);
    }
}
