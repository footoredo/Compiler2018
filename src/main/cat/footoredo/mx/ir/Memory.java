package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;

public class Memory extends Expression {
    Expression expression;

    public Memory(Type type, Expression expression) {
        super(type);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Expression getAddressNode(Type type) {
        return expression;
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
