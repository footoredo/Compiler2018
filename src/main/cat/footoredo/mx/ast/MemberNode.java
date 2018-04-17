package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class MemberNode extends LHSNode {
    protected ExpressionNode expr;
    protected String name;

    public MemberNode (ExpressionNode expr, String name) {
        super ();
        this.expr = expr;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getExpr() {
        return expr;
    }

    public Location getLocation () {
        return expr.getLocation ();
    }
}
