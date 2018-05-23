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
    public Expression addressNode(Type type) {
        return expression;
    }
}
