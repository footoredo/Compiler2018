package cat.footoredo.mx.compiler;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.DefinedFunction;
import cat.footoredo.mx.entity.LocalScope;
import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.ir.*;
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

    private Type asmType (cat.footoredo.mx.type.Type type) {
        return Type.get(type.size());
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
        return null;
    }

    @Override
    public Expression visit(AssignNode node) {
        return null;
    }

    @Override
    public Expression visit(LogicalOpNode node) {
        return null;
    }

    @Override
    public Expression visit(ArithmeticOpNode node) {
        return null;
    }

    @Override
    public Expression visit(BinaryOpNode node) {
        return null;
    }

    @Override
    public Expression visit(UnaryOpNode node) {
        if (node.getOperator().equals("+")) {
            return transformExpression(node.getExpr());
        }
        else {
            return new
        }
        return null;
    }

    @Override
    public Expression visit(PrefixNode node) {
        return null;
    }

    @Override
    public Expression visit(SuffixNode node) {
        return null;
    }

    @Override
    public Expression visit(ComparationNode node) {
        return null;
    }

    @Override
    public Expression visit(ArefNode node) {
        return null;
    }

    @Override
    public Expression visit(MemberNode node) {
        return null;
    }

    @Override
    public Expression visit(FuncallNode node) {
        return null;
    }

    @Override
    public Expression visit(NewNode node) {
        return null;
    }

    @Override
    public Expression visit(VariableNode node) {
        return null;
    }

    @Override
    public Expression visit(IntegerLiteralNode node) {
        return null;
    }

    @Override
    public Expression visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public Expression visit(BooleanLiteralNode node) {
        return null;
    }

    @Override
    public Expression visit(NullLiteralNode node) {
        return null;
    }

    private Type int_t() {
        return Type.get(typeTable.integerSize());
    }

    private Type pointer_t() {
        return Type.get(typeTable.pointerSize());
    }

    private Type bool_t() {
        return Type.get(typeTable.booleanSize());
    }
}
