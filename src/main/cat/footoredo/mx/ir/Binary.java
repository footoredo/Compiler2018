package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;

public class Binary extends Expression {
    private Op op;
    private Expression lhs, rhs;

    public Binary(Type type, Op op, Expression lhs, Expression rhs) {
        super(type);
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Op getOp() {
        return op;
    }

    public Expression getLhs() {
        return lhs;
    }

    public Expression getRhs() {
        return rhs;
    }
}
