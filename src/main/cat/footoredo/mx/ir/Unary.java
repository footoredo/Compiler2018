package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;

public class Unary extends Expression {
    private Op op;
    private Expression expression;

    public Unary(Type type, Op op, Expression expression) {
        super(type);
        this.op = op;
        this.expression = expression;
    }

    public Op getOp() {
        return op;
    }

    public Expression getExpression() {
        return expression;
    }
}
