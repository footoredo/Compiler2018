package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;

public class Integer extends Expression {
    private long value;

    public Integer(Type type, long value) {
        super(type);
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
