package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.ArrayType;
import cat.footoredo.mx.type.Type;

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

    @Override
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        // System.out.println(expr.getLocation());
        return ((ArrayType) expr.getType()).getBaseType();
    }
}
