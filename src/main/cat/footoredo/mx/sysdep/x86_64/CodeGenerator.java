package cat.footoredo.mx.sysdep.x86_64;

import cat.footoredo.mx.asm.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.ir.*;
import cat.footoredo.mx.ir.Integer;
import cat.footoredo.mx.ir.String;
import cat.footoredo.mx.utils.AsmUtils;
import cat.footoredo.mx.utils.ListUtils;
import cat.footoredo.mx.utils.StringUtils;

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
            locateVariable (variable);
        }
        for (Function function: ir.getScope().getAllFunctions()) {
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
        generateTextSection (file, ir.getConstantTable());
        generateCommonSymbols (file, ir.getScope().getUnstaticVariables());
        return file;
    }

    private void generateExterns (AssemblyCode file) {
        file._extern("malloc, _Znam");
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

    private void generateTextSection(AssemblyCode file, ConstantTable constants) {
        file._text();
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
        return AsmUtils.align(size, STACK_WORD_SIZE);
    }

    private long stackSizeFromPosition (long position) {
        return position * STACK_WORD_SIZE;
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
        // System.out.println (function.getName());
        frame.lvarSize = locateLocalVariables(function.getScope());

        AssemblyCode body = compileStatements(function);
        frame.savedRegs = usedCalleeSaveRegisters(body);
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

    private void printStackFrameLayout (AssemblyCode file, StackFrameInfo frame, List<Variable> lvars) {
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
    }

    private AssemblyCode as;
    private Label epilogueLabel;

    private void compileStatement (Statement statement) {
        statement.accept(this);
    }

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
        //System.out.println("jere");
        List<Register> result = new ArrayList<>();
        for (Register register: calleeSaveRegisters()) {
            //System.out.println(register.getNumber());
            //if (register.getNumber() == 7)
            //    System.out.println (body.used(register));
            if (body.used(register)) {
                result.add(register);
            }
        }
        result.remove (bp());
        return result;
    }

    static final long[] CALLEE_SAVE_REGISTERS = {
            RegisterClass.BP.getValue(), RegisterClass.BX.getValue(),
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
                                      StackFrameInfo frame,
                                      List<Parameter> parameters) {
        file.virtualStack.reset();
        prologue (file, frame.savedRegs, frame.frameSize());
        for (Parameter par: parameters) {
            if (par.getParameterSpace().isMemoryReference()) {
                file.mov (ax(), par.getParameterSpace());
                file.mov (par.getMemoryReference(), ax());
            }
            else {
                file.mov (par.getMemoryReference(), (Register)par.getParameterSpace());
            }
        }
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

    static final long[] PARAMETER_REGISTERS = {
            RegisterClass.DI.getValue(), RegisterClass.SI.getValue(),
            RegisterClass.DX.getValue(), RegisterClass.CX.getValue(), 8, 9
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
            var.setParameterSpace(memory(stackSizeFromPosition(currentPosition), bp()));
            currentPosition ++;
        }
    }

    private long locateLocalVariables (LocalScope scope) {
        return locateLocalVariables(scope, 0);
    }

    private long locateLocalVariables (LocalScope scope, long parentStackLength) {
        long length = parentStackLength;
        for (Variable variable: scope.getLocalVariables()) {
            // System.out.println(variable.getName());
            length = alignStack(length + variable.size());
            variable.setMemoryReference(relocatableMemory(-length, bp()));
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
        for (Variable variable: scope.getAllLocalVariables()) {
            variable.getMemoryReference().fixOffset(-length);
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

    private void compile (Expression expression) {
        expression.accept (this);
    }

    @Override
    public Void visit(ExpressionStatement s) {
        compile(s.getExpression());
        return null;
    }

    @Override
    public Void visit(Assign s) {
        // System.out.println (s.getLocation());
        if (s.getLhs().isAddress() && s.getLhs().getMemoryReference() != null) {
            // System.out.println(s.getRhs() instanceof Malloc);
            //System.out.println("1 @" + s.getLocation());
            compile(s.getRhs());
            store (s.getLhs().getMemoryReference(), ax(s.getRhs().getType()));
        }
        else if (s.getRhs().isConstant()) {
            //System.out.println("2 @" + s.getLocation());
            compile(s.getLhs());
            as.mov (cx(), ax());
            loadConstant(ax(), s.getRhs());
            store (memory(cx()), ax(s.getRhs().getType()));
        }
        else {
            // System.out.println(3);
            //System.out.println("3 @" + s.getLocation());
            compile(s.getRhs());
            as.virtualPush(ax());
            compile(s.getLhs());
            as.mov (cx(), ax());
            as.virtualPop(ax());
            store (memory(cx()), ax(s.getRhs().getType()));
        }
        return null;
    }

    @Override
    public Void visit(CJump s) {
        compile(s.getCond());
        Type type = s.getCond().getType();
        as.test (ax(type), ax(type));
        as.j("nz", s.getThenLabel());
        as.jmp(s.getElseLabel());
        return null;
    }

    @Override
    public Void visit(Jump s) {
        as.jmp(s.getTarget());
        return null;
    }

    @Override
    public Void visit(LabelStatement s) {
        as.label(s.getLabel());
        return null;
    }

    @Override
    public Void visit(Return s) {
        if (s.getExpression() != null) {
            compile(s.getExpression());
        }
        as.jmp (epilogueLabel);
        return null;
    }

    @Override
    public Void visit(Null s) {
        as.xor(ax(s.getType()), ax(s.getType()));
        return null;
    }

    @Override
    public Void visit(Unary s) {
        Type src = s.getExpression().getType();
        Type dest = s.getType();

        compile(s.getExpression());
        switch (s.getOp()) {
            case UMINUS:
                as.neg(ax(src));
                break;
            case BIT_NOT:
                as.not (ax(src));
                break;
            case NOT:
                as.test (ax(src), ax(src));
                as.set ("e", al());
                if (dest.size() > 1)
                    as.movzx (ax(dest), al());
        }
        return null;
    }

    @Override
    public Void visit(Binary s) {
        Op op = s.getOp();
        Type type = s.getType();
        Type leftType = s.getLhs().getType();
        Type rightType = s.getRhs().getType();
        if (s.getRhs().isConstant() && !requireRegisterOperand(op)) {
            /*if (op == Op.MUL)
                System.out.println (((Integer)s.getRhs()).getValue());*/
            compile(s.getLhs());
            compileBinaryOp(type, op, ax(leftType), s.getRhs().getAsmValue());
        }
        else if (s.getRhs().isConstant()) {
            //System.out.println (s.getRhs().getAsmValue());
            compile(s.getLhs());
            loadConstant(cx(), s.getRhs());
            compileBinaryOp(type, op, ax(leftType), cx(rightType));
        }
        else if (s.getRhs().isAddress()) {
            compile(s.getLhs());
            loadAddress(cx(rightType), s.getRhs().getEntityForce());
            compileBinaryOp(type, op, ax(leftType), cx(rightType));
        }
        else if (s.getRhs().isVariable()) {
            compile(s.getLhs());
            loadVariable(cx(rightType), (cat.footoredo.mx.ir.Variable)s.getRhs());
            compileBinaryOp(type, op, ax(leftType), cx(rightType));
        }
        else if (s.getLhs().isConstant() || s.getLhs().isVariable() || s.getLhs().isAddress()) {
            compile(s.getRhs());
            as.mov(cx(), ax());
            compile(s.getLhs());
            compileBinaryOp(type, op, ax(leftType), cx(rightType));
        }
        else {
            compile(s.getRhs());
            as.virtualPush(ax());
            compile(s.getLhs());
            as.virtualPop(cx());
            compileBinaryOp(type, op, ax(leftType), cx(rightType));
        }
        return null;
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

    private void compileBinaryOp (Type type, Op op, Register left, Operand right) {
        switch (op) {
            case ADD:
                as.add(left, right);
                break;
            case SUB:
                as.sub(left, right);
                break;
            case MUL:
                as.imul(left, right);
                break;
            case S_DIV:
            case S_MOD:
                as.c(left.getType());
                as.idiv(cx(left.getType()));
                if (op == Op.S_MOD) {
                    as.mov(left, dx());
                }
                break;
            case U_DIV:
            case U_MOD:
                as.mov(dx(), immediate(0));
                as.div(cx(left.getType()));
                if (op == Op.U_MOD) {
                    as.mov (left, dx());
                }
                break;
            case BIT_AND:
                as.and (left, right);
                break;
            case BIT_OR:
                as.or (left, right);
                break;
            case BIT_XOR:
                as.xor (left, right);
                break;
            case BIT_LSHIFT:
                as.shl (left, cl());
                break;
            case BIT_RSHIFT:
                as.shr (left, cl());
                break;
            case ARITH_RSHIFT:
                as.sar (left, cl());
                break;
                default:
                    as.cmp (ax(left.getType()), right);
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
                    if (left.getType().size() > 1)
                        as.movzx (left, al());
                    else
                        as.mov (left, al());
        }
    }

    @Override
    public Void visit(Call s) {
        for (int i = 0; i < s.getArgc() && i < PARAMETER_REGISTERS.length; ++ i) {
            Expression arg = s.getArg(i);
            compile(arg);
            as.mov (new Register(PARAMETER_REGISTERS[i]), ax());
        }
        for (int i = (int)s.getArgc() - 1; i >= PARAMETER_REGISTERS.length; -- i) {
            compile(s.getArg(i));
            as.push (ax());
            // as.mov (s.getFunction().getParameter(i).getMemoryReference(), ax());
        }
        // System.out.println (call.)
        as.call(s.getFunction().getCallingSymbol());
        return null;
    }

    @Override
    public Void visit(Address s) {
        loadAddress(ax(), s.getEntity());
        return null;
    }

    @Override
    public Void visit(Memory s) {
        compile(s.getExpression());
        load (ax(s.getType()), memory(ax()));
        return null;
    }

    @Override
    public Void visit(cat.footoredo.mx.ir.Variable s) {
        loadVariable(ax(), s);
        return null;
    }

    @Override
    public Void visit(Integer s) {
        as.mov (ax(), immediate(s.getValue()));
        return null;
    }

    @Override
    public Void visit(String s) {
        loadConstant(ax(), s);
        return null;
    }

    @Override
    public Void visit(Malloc s) {
        compile(s.getSize());
        as.mov (new Register(PARAMETER_REGISTERS[0]), ax());
        as.call(new NamedSymbol("malloc"));
        return null;
    }

    private IndirectMemoryReference relocatableMemory(long offset, Register base) {
        return IndirectMemoryReference.relocatable(offset, base);
    }

    private void loadConstant (Register dest, Expression node) {
        if (node.getAsmValue() != null) {
            as.mov (dest, node.getAsmValue());
        }
        else if (node.getMemoryReference() != null) {
            as.lea (dest, node.getMemoryReference());
        }
        else {
            throw new Error("must not happen: constant has no asm value");
        }
    }

    private void loadVariable (Register dest, cat.footoredo.mx.ir.Variable var) {
        /*if (var.getAddress() != null) {
            // System.out.println (var.getName() + " is address" + var.getAddress().toSource(SymbolTable.dummy()));
            Register a = dest.forType(naturalType);
            as.mov (a, var.getAddress());
            load (dest.forType(var.getType()), memory(a));
        }
        else*/ if (var.getMemoryReference() != null ) {
            // System.out.println (var.getName() + var.getType());
            load (dest.forType(var.getType()), var.getMemoryReference());
        }
        else {
            as.mov (dest, var.getRegister());
        }
    }

    private void loadAddress (Register dest, Entity var) {
        if (var.getAddress() != null) {
            as.mov (dest, var.getAddress());
        }
        else {
            as.lea (dest, var.getMemoryReference());
            // throw new Error ("???");
        }
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

    private DirectMemoryReference memory (Symbol symbol) {
        return new DirectMemoryReference(symbol);
    }

    private IndirectMemoryReference memory (Register base) {
        return new IndirectMemoryReference(0, base);
    }

    private IndirectMemoryReference memory (long offset, Register base) {
        return new IndirectMemoryReference(offset, base);
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
