package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class BinaryOpNode extends ExpressionNode {
    protected ExpressionNode lhs, rhs;
    protected String operator;

    public BinaryOpNode (ExpressionNode lhs, String operator, ExpressionNode rhs) {
        super ();
        this.lhs = lhs;
        this.operator = operator;
        this.rhs = rhs;
    }

    public Location getLocation() {
        return lhs.getLocation();
    }

    public ExpressionNode getRhs() {
        return rhs;
    }

    public ExpressionNode getLhs() {
        return lhs;
    }

    public String getOperator() {
        return operator;
    }
}
