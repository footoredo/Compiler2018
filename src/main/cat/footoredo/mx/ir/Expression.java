package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;

abstract public class Expression {
    private final Type type;

    public Expression(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Expression addressNode (Type type) {
        throw new Error("unexpected node for LHS: " + getClass());
    }
}
