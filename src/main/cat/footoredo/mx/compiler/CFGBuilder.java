package cat.footoredo.mx.compiler;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.asm.NamedSymbol;
import cat.footoredo.mx.asm.Symbol;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.cfg.*;
import cat.footoredo.mx.entity.DefinedFunction;
import cat.footoredo.mx.ir.*;
import cat.footoredo.mx.ir.Integer;
import cat.footoredo.mx.ir.String;
import cat.footoredo.mx.type.BooleanType;
import cat.footoredo.mx.type.PointerType;

import java.util.*;

public class CFGBuilder implements IRVisitor<Void, Operand> {
    private CFG cfg;

    public CFG generateCFG (IR ir) {
        cfg = new CFG();

        for (DefinedFunction definedFunction: ir.getAllDefinedFunctions()) {
            currentFunction = definedFunction;
            processDefinedFunction (definedFunction);
            visitedBasicBlocks = new HashSet<>();
            dfsAndLink (definedFunction.getStartBasicBlock());
            backPropagate (definedFunction.getEndBasicBlock(), new HashSet<>(/*Arrays.asList((cat.footoredo.mx.entity.Variable)(ir.getScope().get("thisPointer")))*/));
            visitedBasicBlocks = new HashSet<>();
            dfsAndClean (definedFunction.getStartBasicBlock());
        }

        return cfg;
    }

    private DefinedFunction currentFunction;
    private Set<BasicBlock> visitedBasicBlocks;

    private void dfsAndLink (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add (currentBasicBlock);
        if (currentBasicBlock.isEndBlock()) {
            currentFunction.setEndBasicBlock(currentBasicBlock);
        }
        for (Label outputLabel: currentBasicBlock.getOutputLabels()) {
            BasicBlock nextBasicBlock = cfg.get (outputLabel);
            currentBasicBlock.addOutput(nextBasicBlock);
            nextBasicBlock.addInput(currentBasicBlock);
            if (!visitedBasicBlocks.contains(nextBasicBlock)) {
                dfsAndLink(nextBasicBlock);
            }
        }
    }

    private void dfsAndClean (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add (currentBasicBlock);
        currentBasicBlock.cleanInstructions();
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndClean(output);
    }

    private void backPropagate (BasicBlock currentBasicBlock, Set <cat.footoredo.mx.entity.Variable> liveVariables) {
        boolean isUpdated = false;
        if (!currentBasicBlock.backPropageted()) {
            isUpdated = true;
            currentBasicBlock.prepareForFirstBackPropagate();
        }
        for (cat.footoredo.mx.entity.Variable variable: liveVariables) {
            // System.out.println (variable.getName());
            if (!currentBasicBlock.isLiveVariable(variable)) {
                isUpdated = true;
                currentBasicBlock.addLiveVariable(variable);
            }
        }
        if (isUpdated) {
            Set <cat.footoredo.mx.entity.Variable> newLiveVariables = currentBasicBlock.backPropagate ();
            for (BasicBlock input: currentBasicBlock.getInputs()) {
                backPropagate(input, liveVariables);
            }
        }
    }

    private BasicBlock currentBasicBlock;
    private void processDefinedFunction (DefinedFunction definedFunction) {
        Symbol symbol = new NamedSymbol("@@CFG_function_start__" + definedFunction.getSymbolString());
        Label label = new Label(symbol);
        definedFunction.setCFGSymbol (symbol);
        newBasicBlock(label);
        definedFunction.setStartBasicBlock(currentBasicBlock);
        for (Statement statement: definedFunction.getIR()) {
            processStatement(statement);
        }
        if (currentBasicBlock == null)
            throw new Error("FFFAFAF");
        insert (new ULTIMATERETURNINST());
        currentBasicBlock = null;
    }

    private VariableOperand tmpVariable (Type type) {
        cat.footoredo.mx.type.Type typeType = type == Type.INT64 ? new PointerType() : new BooleanType();
        return new VariableOperand(currentFunction.getScope().allocateTmpVariable(typeType));
    }

    private void newBasicBlock (Label label) {
        currentBasicBlock = new BasicBlock(label);
        cfg.put (label, currentBasicBlock);
    }

    private Operand processExpression (Expression expression) {
        return expression.accept(this);
    }

    private void processStatement (Statement statement) {
        statement.accept(this);
    }

    private void insert (Instruction instruction) {
        if (currentBasicBlock != null)
            currentBasicBlock.addInstruction(instruction);
    }

    private void jump (JumpInst jumpInst) {
        if (currentBasicBlock != null)
            currentBasicBlock.setJumpInst(jumpInst);
        currentBasicBlock = null;
    }

    private Operand operand (Variable variable) {
        return new VariableOperand((cat.footoredo.mx.entity.Variable) variable.getEntity());
    }

    private Operand operand (Integer integerLiteral) {
        return new ConstantIntegerOperand(integerLiteral.getType(), integerLiteral.getValue());
    }

    private Operand operand (String stringLiteral) {
        return new ConstantStringOperand(stringLiteral.getEntry());
    }

    @Override
    public Void visit(ExpressionStatement s) {
        processExpression(s.getExpression());
        return null;
    }

    @Override
    public Void visit(Assign s) {
        Operand right = processExpression(s.getRhs());
        insert (new AssignInst(processExpression(s.getLhs()), right, s.getLhs() instanceof Memory));
        return null;
    }

    private void cjump (Operand condition, Label trueLabel, Label falseLabel) {
        if (condition instanceof ConstantIntegerOperand) {
            boolean value = ((ConstantIntegerOperand) condition).getValue() != 0;
            if (value) {
                jump (new UnconditionalJumpInst(trueLabel));
            }
            else {
                jump (new UnconditionalJumpInst(falseLabel));
            }
        }
        else {
            jump (new ConditionalJumpInst(condition, trueLabel, falseLabel));
        }
    }

    @Override
    public Void visit(CJump s) {
        cjump (processExpression(s.getCond()), s.getThenLabel(), s.getElseLabel());
        return null;
    }

    @Override
    public Void visit(Jump s) {
        jump (new UnconditionalJumpInst(s.getTarget()));
        return null;
    }

    @Override
    public Void visit(LabelStatement s) {
        if (currentBasicBlock != null) {
            jump (new UnconditionalJumpInst(s.getLabel()));
        }
        newBasicBlock(s.getLabel());
        return null;
    }

    @Override
    public Void visit(Return s) {
        if (s.hasExpression()) {
            // System.out.println ("asd");
            insert (new ReturnInst(processExpression(s.getExpression())));
        }
        else {
            insert (new ReturnInst());
        }
        jump (new UnconditionalJumpInst(s.getFunctionEndLabel()));
        return null;
    }

    @Override
    public Operand visit(Null s) {
        return new ConstantIntegerOperand(Type.INT64, 0);
    }

    @Override
    public Operand visit(Unary s) {
        VariableOperand tmp = tmpVariable (s.getType());
        insert (new UnaryInst(tmp, s.getOp(), processExpression(s.getExpression())));
        return tmp;
    }

    private Operand integer (Type t, long value) {
        return new ConstantIntegerOperand(t, value);
    }

    private Operand integer (Type t, boolean value) {
        return new ConstantIntegerOperand(t, value ? 1 : 0);
    }


    private Operand preprocessBinary(Type t, long left, Op op, long right) {
        switch (op) {
            case ADD: return integer(t, left + right);
            case SUB: return integer(t, left - right);
            case MUL: return integer(t, left * right);
            case S_DIV: return integer(t, left / right);
            case S_MOD: return integer(t, left % right);
            case U_DIV: return integer(t, left / right);
            case U_MOD: return integer(t, left % right);
            case BIT_AND: return integer(t, left & right);
            case BIT_OR: return integer(t, left | right);
            case BIT_XOR: return integer(t, left ^ right);
            case BIT_LSHIFT: return integer(t, left << right);
            case BIT_RSHIFT: return integer(t, left >> right);
            case ARITH_RSHIFT: return integer(t, left << right);
            default:
                switch (op) {
                    case EQ: return integer(t, left == right);
                    case NEQ: return integer(t, left != right);
                    case S_GT: return integer(t, left > right);
                    case S_GTEQ: return integer(t, left >= right);
                    case S_LT: return integer(t, left < right);
                    case S_LTEQ: return integer(t, left <= right);
                    case U_GT: return integer(t, left > right);
                    case U_GTEQ: return integer(t, left >= right);
                    case U_LT: return integer(t, left < right);
                    case U_LTEQ: return integer(t, left <= right);
                    default:
                        throw new Error ("unknown binary operator: " + op);
                }
        }
    }

    @Override
    public Operand visit(Binary s) {
        Operand right = processExpression(s.getRhs());
        Operand left = processExpression(s.getLhs());
        if (left instanceof ConstantIntegerOperand && right instanceof ConstantIntegerOperand) {
            return preprocessBinary (s.getType(), ((ConstantIntegerOperand) left).getValue(), s.getOp(), ((ConstantIntegerOperand) right).getValue());
        }
        else {
            VariableOperand tmp = tmpVariable(s.getType());
            insert(new BinaryInst(tmp, left, s.getOp(), right));
            return tmp;
        }
    }

    @Override
    public Operand visit(Call s) {
        List<Operand> parameters = new ArrayList<>();
        for (int i = 0; i < s.getArgc(); ++ i) {
            parameters.add (processExpression(s.getArg(i)));
        }
        VariableOperand tmp = tmpVariable (s.getType());
        insert (new CallInst(tmp, s.getFunction(), parameters));
        return tmp;
    }

    @Override
    public Operand visit(Memory s) {
        VariableOperand tmp = tmpVariable (s.getType());
        insert (new DereferenceInst(tmp, processExpression(s.getExpression())));
        return tmp;
    }

    @Override
    public Operand visit(Variable s) {
        return new VariableOperand((cat.footoredo.mx.entity.Variable) s.getEntity());
    }

    @Override
    public Operand visit(Integer s) {
        return new ConstantIntegerOperand(s.getType(), s.getValue());
    }

    @Override
    public Operand visit(String s) {
        return new ConstantStringOperand(s.getEntry());
    }

    @Override
    public Operand visit(Malloc s) {
        VariableOperand tmp = tmpVariable(Type.INT64);
        insert (new MallocInst(tmp, processExpression(s.getSize())));
        return tmp;
    }
}
