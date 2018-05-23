package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.ConstantEntry;

public class String extends Expression {
    private java.lang.String value;

    public String(Type type, java.lang.String value) {
        super(type);
        this.value = value;
    }

    public java.lang.String getValue() {
        return value;
    }
}
