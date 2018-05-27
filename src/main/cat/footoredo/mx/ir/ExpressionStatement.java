package cat.footoredo.mx.ir;

import cat.footoredo.mx.entity.Location;

public class ExpressionStatement extends Statement {
    private Expression expression;

    public ExpressionStatement(Location location, Expression expression) {
        super(location);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <S, E> S accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
