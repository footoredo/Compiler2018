package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ArefNode extends LHSNode {
    private ExpressionNode expr, index;

    public ArefNode(ExpressionNode expr, ExpressionNode index) {
        this.expr = expr;
        this.index = index;
    }

    public ExpressionNode getExpr() {
        return expr;
    }

    public ExpressionNode getIndex() {
        return index;
    }

    public Location getLocation () {
        return expr.getLocation();
    }
}
