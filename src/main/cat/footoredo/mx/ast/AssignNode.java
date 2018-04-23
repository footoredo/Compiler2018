package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.Type;

public class AssignNode extends ExpressionNode {
    protected ExpressionNode lhs, rhs;

    public AssignNode (ExpressionNode lhs, ExpressionNode rhs) {
        super ();
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Location getLocation() {
        return lhs.getLocation();
    }

    public ExpressionNode getLhs() {
        return lhs;
    }

    public ExpressionNode getRhs() {
        return rhs;
    }

    @Override
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        return lhs.getType();
    }
}
