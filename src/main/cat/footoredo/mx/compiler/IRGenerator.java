package cat.footoredo.mx.compiler;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.ir.*;
import cat.footoredo.mx.ir.Integer;
import cat.footoredo.mx.ir.String;
import cat.footoredo.mx.ir.Variable;
import cat.footoredo.mx.type.TypeTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IRGenerator implements ASTVisitor<Void, Expression> {
    private final TypeTable typeTable;

    public IRGenerator(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    public IR generate(AST ast) throws SemanticException {
        for (Variable variable: ast.getVariables()) {
            if (variable.hasInitializer())
                variable.setIr(transformExpression(variable.getInitializer()));
        }
        for (DefinedFunction f: ast.getFunctions()) {
            f.setIR(compileFunctionBody(f))
        }
    }

    private Statement transformStatement(StatementNode statementNode) {
        return null;
    }

    private int expressionLevel = 0;

    private Expression transformExpression(ExpressionNode expressionNode) {
        expressionLevel ++;
        Expression e = expressionNode.accept(this);
        expressionLevel --;
        return e;
    }

    private boolean isStatement() {
        return expressionLevel == 0;
    }

    List<Statement> statements;
    LinkedList<LocalScope> scopeStack;
    LinkedList<Label> breakStack;
    LinkedList<Label> continueStack;

    private List<Statement> compileFunctionBody (DefinedFunction function) {
        statements = new ArrayList<>();
        scopeStack = new LinkedList<>();
        breakStack = new LinkedList<>();
        continueStack = new LinkedList<>();

        transformStatement(function.getBlock());
        return statements;
    }

    private void label (Label label) {
        statements.add (new LabelStatement(null, label));
    }

    private void jump (Location location, Label target) {
        statements.add(new Jump(location, target));
    }

    private void jump (Label target) {
        jump (null, target);
    }

    private void cjump (Location location, Expression cond, Label thenLabel, Label elseLabel) {
        statements.add (new CJump(location, cond, thenLabel, elseLabel));
    }

    private void pushBreak (Label label) {
        breakStack.add (label);
    }

    private void popBreak () {
        breakStack.removeLast();
    }

    private Label currentBreakTarget () {
        return breakStack.getLast();
    }

    private void pushContinue (Label label) {
        continueStack.add (label);
    }

    private void popContinue () {
        continueStack.removeLast();
    }

    private Label currentContinueTarget () {
        return continueStack.getLast();
    }

    private void assign (Location location, Expression lhs, Expression rhs) {
        statements.add (new Assign(location, addressOf(lhs), rhs));
    }

    private cat.footoredo.mx.entity.Variable tmpVariable (cat.footoredo.mx.type.Type type) {
        return scopeStack.getLast().allocateTmpVariable(type);
    }

    private Expression transformOpAssign (Location location, Op op, Expression lhs, Expression rhs) {
        assign (location, lhs, binary(op, lhs, rhs));
    }

    private Binary binary (Op op, Expression lhs, Expression rhs) {
        return new Binary(lhs.getType(), op, lhs, rhs);
    }

    private Expression transformIndex (ArefNode node) {
        return transformExpression(node.getIndex());
    }

    @Override
    public Void visit(BlockNode node) {
        return null;
    }

    @Override
    public Void visit(ExpressionStatementNode node) {
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        Label thenLabel = new Label();
        Label elseLabel = new Label();
        Label endLabel = new Label();

        Expression cond = transformExpression(node.getJudge());

        if (node.hasElseStatement()) {
            cjump (node.getLocation(), cond, thenLabel, elseLabel);
            label (thenLabel);
            transformStatement(node.getThenStatement());
            jump (endLabel);
            label (elseLabel);
            transformStatement (node.getElseStatement());
            label (endLabel);
        }
        else {
            cjump (node.getLocation(), cond, thenLabel, endLabel);
            label (thenLabel);
            transformStatement (node.getThenStatement());
            label (endLabel);
        }

        return null;
    }

    @Override
    public Void visit(WhileNode node) {
        Label beginLabel = new Label();
        Label bodyLabel = new Label();
        Label endLabel = new Label();

        label (beginLabel);
        cjump (node.getLocation(), transformExpression(node.getJudge()), bodyLabel, endLabel);

        label (bodyLabel);
        pushContinue(beginLabel);
        pushBreak(endLabel);

        transformStatement(node.getBody());

        popBreak();
        popContinue();

        jump (beginLabel);
        label (endLabel);

        return null;
    }

    @Override
    public Void visit(ForNode node) {
        Label beginLabel = new Label();
        Label bodyLabel = new Label();
        Label endLabel = new Label();

        if (node.hasInit())
            transformStatement(node.getInit());

        label (beginLabel);
        if (node.hasJudge())
            cjump (node.getLocation(), transformExpression(node.getJudge()), bodyLabel, endLabel);

        label (bodyLabel);
        pushContinue(beginLabel);
        pushBreak(endLabel);

        transformStatement(node.getBody());
        if (node.hasUpdate())
            transformExpression(node.getUpdate());

        popBreak();
        popContinue();

        jump (beginLabel);
        label (endLabel);

        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        jump (node.getLocation(), currentBreakTarget());
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        jump (node.getLocation(), currentContinueTarget());
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        statements.add(new Return(node.getLocation(), node.hasExpression() ? transformExpression(node.getExpression()) : null));
        return null;
    }

    @Override
    public Void visit(NullStatementNode node) {
        return null;
    }

    @Override
    public Void visit(LocalVariableDeclarationNode node) {
        if (node.hasInitialExpression()) {
            cat.footoredo.mx.entity.Variable variable = node.getVariable();
            assign (variable.getLocation(), ref (variable), transformExpression(node.getInitialExpression()));
        }
        return null;
    }

    @Override
    public Expression visit(AssignNode node) {
        Location lhsLocation = node.getLhs().getLocation();
        Location rhsLocation = node.getRhs().getLocation();
        if (isStatement()) {
            Expression rhs = transformExpression(node.getRhs());
            assign (lhsLocation, transformExpression(node.getLhs()), rhs);
            return null;
        }
        else {
            cat.footoredo.mx.entity.Variable tmp = tmpVariable(node.getRhs().getType());
            cat.footoredo.mx.ir.Variable tmpVar = ref (tmp);
            assign (rhsLocation, tmpVar, transformExpression(node.getRhs()));
            assign (lhsLocation, transformExpression(node.getLhs()), tmpVar);
            return tmpVar;
        }
    }

    @Override
    public Expression visit(LogicalOpNode node) {
        java.lang.String operator = node.getOperator();
        assert (operator.equals("||") || operator.equals("&&"));

        Label rightLabel = new Label ();
        Label endLabel = new Label ();

        cat.footoredo.mx.entity.Variable variable = tmpVariable(node.getType());
        assign (node.getLhs().getLocation(), ref(variable), transformExpression(node.getLhs()));

        if (operator.equals("&&")) {
            cjump(node.getLocation(), ref(variable), rightLabel, endLabel);
        }
        else {
            cjump(node.getLocation(), ref(variable), endLabel, rightLabel);
        }

        label (rightLabel);
        assign (node.getRhs().getLocation(), ref (variable), transformExpression(node.getRhs()));

        label (endLabel);

        return isStatement() ? null : ref (variable);
    }

    @Override
    public Expression visit(ArithmeticOpNode node) {
        Expression rhs = transformExpression(node.getRhs());
        Expression lhs = transformExpression(node.getLhs());
        Op op = Op.internBinary(node.getOperator(), node.getType().isSigned());
        cat.footoredo.mx.type.Type type = node.getType();
        return isStatement() ? null : new Binary(asmType(type), op, lhs, rhs);
    }

    @Override
    public Expression visit(UnaryOpNode node) {
        if (node.getOperator().equals("+")) {
            return transformExpression(node.getExpression());
        }
        else {
            return new Unary(asmType(node.getType()), Op.internUnary(node.getOperator()), transformExpression(node.getExpression()));
        }
    }

    @Override
    public Expression visit(PrefixNode node) {
        cat.footoredo.mx.type.Type type = node.getType();
        return transformOpAssign(node.getLocation(), binaryOp(node.getOperator()),
                transformExpression(node.getExpression()), immediate(type, 1));
    }

    @Override
    public Expression visit(SuffixNode node) {
        Expression expression = transformExpression(node.getExpression());
        cat.footoredo.mx.type.Type type = node.getType();
        Op op = binaryOp(node.getOperator());
        Location location = node.getLocation();

        if (isStatement()) {
            transformOpAssign(location, op, expression, immediate(type, 1));
            return null;
        }
        else {
            cat.footoredo.mx.entity.Variable variable = tmpVariable(type);
            assign (location, ref (variable), expression);
            assign (location, expression, binary(op, ref (v), immediate(type, 1)));
            return ref (variable);
        }
    }

    @Override
    public Expression visit(ComparationNode node) {
        Expression rhs = transformExpression(node.getRhs());
        Expression lhs = transformExpression(node.getLhs());
        Op op = Op.internBinary(node.getOperator(), node.getType().isSigned());
        cat.footoredo.mx.type.Type type = node.getType();
        if (node.getLhs().getType().isString()) {
            // TODO..
            return null;
        }
        else {
            return isStatement() ? null : new Binary(asmType(type), op, lhs, rhs);
        }
    }

    @Override
    public Expression visit(ArefNode node) {
        Expression expression = transformExpression(node.getExpression());
        Expression offset = new Binary(ptrdiff_t(), Op.MUL, size (node.elementSize()), transformIndex(node));
        Binary address = new Binary(ptr_t(), Op.ADD, expression, offset);
        return memory(address, node.getType());
    }

    @Override
    public Expression visit(MemberNode node) {
        if (node.getType().isFunction()) {
            // TODO..
        }
        else {
            Expression expression = addressOf(transformExpression(node.getExpression()));
            Expression offset = ptrdiff(node.getOffset());
            Expression address = new Binary(ptr_t(), Op.ADD, expression, offset);
            return memory(address, node.getType());
        }
        return null;
    }

    @Override
    public Expression visit(FuncallNode node) {
        List<Expression> args = new ArrayList<>();
        for (ExpressionNode arg : node.getParams()) {
            args.add(transformExpression(arg));
        }
        Expression call = new Call (asmType(node.getReturnType()), transformExpression(node.getCaller()), args);
        if (isStatement()) {
            statements.add (new ExpressionStatement(node.getLocation(), call));
            return null;
        }
        else {
            cat.footoredo.mx.entity.Variable tmp = tmpVariable(node.getReturnType());
            assign(node.getLocation(), ref (tmp), call);
            return ref (tmp);
        }
    }

    @Override
    public Expression visit(NewNode node) {
        return null;
    }

    @Override
    public Expression visit(VariableNode node) {
        cat.footoredo.mx.ir.Variable variable = ref (node.getEntity());
        return node.isLoadable() ? variable : addressOf(variable);
    }

    @Override
    public Expression visit(IntegerLiteralNode node) {
        return new Integer(asmType(node.getType()), node.getValue());
    }

    @Override
    public Expression visit(StringLiteralNode node) {
        return new String(asmType(node.getType()), node.getValue());
    }

    @Override
    public Expression visit(BooleanLiteralNode node) {
        return new Integer(asmType(node.getType()), node.getValue() ? 1 : 0);
    }

    @Override
    public Expression visit(NullLiteralNode node) {
        return new Null(asmType(node.getType()));
    }

    private Expression addressOf (Expression expression) {
        return expression.addressNode(ptr_t());
    }

    private Type asmType (cat.footoredo.mx.type.Type type) {
        return Type.get(type.size());
    }

    private Type varType (cat.footoredo.mx.type.Type type) {
        return Type.get(type.size());
    }

    private cat.footoredo.mx.ir.Variable ref (Entity entity) {
        return new cat.footoredo.mx.ir.Variable(varType(entity.getType()), entity);
    }

    private Op binaryOp (java.lang.String unaryOp) {
        return unaryOp.equals("++") ? Op.ADD : Op.SUB;
    }

    private Memory memory(Entity entity) {
        return new Memory(asmType(entity.getType()), ref(entity));
    }

    private Memory memory(Expression expression, cat.footoredo.mx.type.Type type) {
        return new Memory(asmType(type), expression);
    }

    private Integer immediate (cat.footoredo.mx.type.Type type, long value) {
        return new Integer(int_t(), value);
    }

    private Integer size (long value) {
        return new Integer(size_t(), value);
    }

    private Integer ptrdiff (long value) {
        return new Integer(ptrdiff_t(), value);
    }

    private Type int_t() {
        return Type.get(TypeTable.integerSize);
    }

    private Type ptr_t() {
        return Type.get(TypeTable.pointerSize);
    }

    private Type ptrdiff_t() {
        return Type.get(TypeTable.longSize);
    }

    private Type size_t() {
        return Type.get(TypeTable.longSize);
    }

    private Type bool_t() {
        return Type.get(TypeTable.booleanSize);
    }
}
