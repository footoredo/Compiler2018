package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ExpressionStatementNode extends StatementNode {
    private ExprNode expr;

    ExpressionStatementNode ( ExprNode expr) {
        super ();
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public Location getLocation () {
        return expr.getLocation();
    }
}
