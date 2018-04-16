package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class BinaryOpNode extends ExprNode {
    protected ExprNode lhs, rhs;
    protected String operator;

    public BinaryOpNode (ExprNode lhs, String operator, ExprNode rhs) {
        super ();
        this.lhs = lhs;
        this.operator = operator;
        this.rhs = rhs;
    }

    public Location getLocation() {
        return lhs.getLocation();
    }

    public ExprNode getRhs() {
        return rhs;
    }

    public ExprNode getLhs() {
        return lhs;
    }

    public String getOperator() {
        return operator;
    }
}
