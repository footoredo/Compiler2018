package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class UnaryOpNode extends ExprNode {
    private Location location;
    private String operator;
    private ExprNode expr;
    private boolean opFront;

    public UnaryOpNode (Location location, String operator, ExprNode expr, boolean opFront) {
        super ();
        this.location = location;
        this.operator = operator;
        this.expr = expr;
        this.opFront = opFront;
    }

    public String getOperator() {
        return operator;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public boolean isOpFront() {
        return opFront;
    }

    public boolean isOpBack () {
        return !opFront;
    }

    public Location getLocation() {
        return location;
    }
}
