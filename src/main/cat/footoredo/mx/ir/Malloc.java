package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;

public class Malloc extends Expression {
    private Expression size;

    public Malloc(Type type, Expression size) {
        super(type);
        this.size = size;
    }

    public Expression getSize() {
        return size;
    }
}
