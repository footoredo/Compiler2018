package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.ArrayType;
import cat.footoredo.mx.type.Type;

public class ArefNode extends LHSNode {
    private ExpressionNode expression, index;

    @Override
    public boolean isMemorable() {
        return expression.isMemorable() && index.isMemorable();
    }

    public ArefNode(ExpressionNode expression, ExpressionNode index) {
        this.expression = expression;
        this.index = index;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public ExpressionNode getIndex() {
        return index;
    }

    public Location getLocation () {
        return expression.getLocation();
    }

    public int elementSize () {
        return getType().size();
    }

    public Type getElementType () {
        return ((ArrayType) expression.getType()).getBaseType();
    }

    @Override
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        // System.out.println(expression.getLocation());
        return getElementType();
    }
}
