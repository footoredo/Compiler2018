package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.ImmediateValue;
import cat.footoredo.mx.asm.Type;

public class ConstantIntegerOperand extends Operand {
    private long value;

    public ConstantIntegerOperand(Type type, long value) {
        super (type);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public ImmediateValue getImmediateValue() {
        return new ImmediateValue(value);
    }
}
