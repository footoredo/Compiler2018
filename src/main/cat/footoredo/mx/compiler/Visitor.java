package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.*;

import java.util.List;

public class Visitor implements ASTVisitor<Void, Void> {
    protected void visitStatement(StatementNode statement) {
        statement.accept(this);
    }

    protected void visitStatements(List<? extends StatementNode> statements) {
        for (StatementNode s: statements) {
            visitStatement(s);
        }
    }

    protected void visitExpression(ExpressionNode expression) {
        expression.accept(this);
    }

    protected void visitExpressions(List<? extends ExpressionNode> expressions) {
        for (ExpressionNode e: expressions) {
            visitExpression(e);
        }
    }

    @Override
    public Void visit(BlockNode node) {
        visitStatements(node.getStatements());
        return null;
    }

    @Override
    public Void visit(ExpressionStatementNode node) {
        visitExpression(node.getExpr());
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        visitExpression(node.getJudge());
        visitStatement(node.getThenStatement());
        if (node.hasElseStatement()) {
            visitStatement(node.getElseStatement());
        }
        return null;
    }

    @Override
    public Void visit(WhileNode node) {
        visitExpression(node.getJudge());
        visitStatement(node.getBody());
        return null;
    }

    @Override
    public Void visit(ForNode node) {
        if (node.hasInit()) visitExpression(node.getInit());
        if (node.hasJudge()) visitExpression(node.getJudge());
        if (node.hasUpdate()) visitExpression(node.getUpdate());
        visitStatement(node.getBody());
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        if (node.hasExpr()) visitExpression(node.getExpr());
        return null;
    }

    @Override
    public Void visit(NullStatementNode node) {
        return null;
    }

    @Override
    public Void visit(LocalVariableDeclarationNode node) {
        if (node.hasInitExpr()) visitExpression(node.getInitExpr());
        return null;
    }

    @Override
    public Void visit(AssignNode node) {
        visitExpression(node.getLhs());
        visitExpression(node.getRhs());
        return null;
    }

    @Override
    public Void visit(LogicalOpNode node) {
        visitExpression(node.getLhs());
        visitExpression(node.getRhs());
        return null;
    }

    @Override
    public Void visit(BinaryOpNode node) {
        visitExpression(node.getLhs());
        visitExpression(node.getRhs());
        return null;
    }

    @Override
    public Void visit(UnaryOpNode node) {
        visitExpression(node.getExpr());
        return null;
    }

    @Override
    public Void visit(PrefixNode node) {
        visitExpression(node.getExpr());
        return null;
    }

    @Override
    public Void visit(SuffixNode node) {
        visitExpression(node.getExpr());
        return null;
    }

    @Override
    public Void visit(ArefNode node) {
        visitExpression(node.getExpr());
        visitExpression(node.getIndex());
        return null;
    }

    @Override
    public Void visit(MemberNode node) {
        visitExpression(node.getExpr());
        return null;
    }

    @Override
    public Void visit(FuncallNode node) {
        visitExpression(node.getCaller());
        visitExpressions(node.getParams());
        return null;
    }

    @Override
    public Void visit(NewNode node) {
        if (node.hasArgs()) visitExpressions(node.getArgs());
        if (node.hasLengths()) visitExpressions(node.getLengths());
        return null;
    }

    @Override
    public Void visit(VariableNode node) {
        return null;
    }

    @Override
    public Void visit(IntegerLiteralNode node) {
        return null;
    }

    @Override
    public Void visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public Void visit(BooleanLiteralNode node) {
        return null;
    }
}
