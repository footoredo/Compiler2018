package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ExpressionStatementNode extends StatementNode {
    private ExpressionNode expr;

    ExpressionStatementNode ( ExpressionNode expr) {
        super ();
        this.expr = expr;
    }

    public ExpressionNode getExpr() {
        return expr;
    }

    public Location getLocation () {
        return expr.getLocation();
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
