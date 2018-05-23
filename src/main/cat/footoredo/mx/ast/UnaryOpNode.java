package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.Type;

public class UnaryOpNode extends ExpressionNode {
    private Location location;
    private String operator;
    private ExpressionNode expression;

    public UnaryOpNode (Location location, String operator, ExpressionNode expression) {
        super ();
        this.location = location;
        this.operator = operator;
        this.expression = expression;
    }

    public String getOperator() {
        return operator;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        return expression.getType();
    }
}
