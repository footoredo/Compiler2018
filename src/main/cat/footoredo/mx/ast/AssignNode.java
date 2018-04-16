package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class AssignNode extends ExprNode {
    protected ExprNode lhs, rhs;

    public AssignNode (ExprNode lhs, ExprNode rhs) {
        super ();
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    Location getLocation() {
        return lhs.getLocation();
    }

    public ExprNode getLhs() {
        return lhs;
    }

    public ExprNode getRhs() {
        return rhs;
    }
}
