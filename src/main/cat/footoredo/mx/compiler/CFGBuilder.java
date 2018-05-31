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

import java.util.ArrayList;
import java.util.List;

public class CFGBuilder implements IRVisitor<Void, Operand> {
    private CFG cfg;

    public CFG generateCFG (IR ir) {
        cfg = new CFG();
        for (DefinedFunction definedFunction: ir.getAllDefinedFunctions()) {
            processDefinedFunction (definedFunction);
        }
        return cfg;
    }

    private BasicBlock currentBasicBlock;
    private DefinedFunction currentFunction;
    private void processDefinedFunction (DefinedFunction definedFunction) {
        Symbol symbol = new NamedSymbol("@@CFG_function_start__" + definedFunction.getSymbolString());
        Label label = new Label(symbol);
        definedFunction.setCFGSymbol (symbol);
        newBasicBlock(label);
        currentFunction = definedFunction;
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

    @Override
    public Void visit(CJump s) {
        jump (new ConditionalJumpInst(processExpression(s.getCond()), s.getThenLabel(), s.getElseLabel()));
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

    @Override
    public Operand visit(Binary s) {
        VariableOperand tmp = tmpVariable(s.getType());
        insert (new BinaryInst(tmp, processExpression(s.getLhs()), s.getOp(), processExpression(s.getRhs())));
        return tmp;
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
