package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.ImmediateValue;
import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Symbol;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.ConstantEntry;

public class ConstantStringOperand extends Operand {
    ConstantEntry entry;

    public ConstantStringOperand(ConstantEntry entry) {
        super (Type.INT64);
        this.entry = entry;
    }

    public Symbol getEntry() {
        return entry.getSymbol();
    }

    public String getValue () {
        return entry.getValue();
    }

    @Override
    public String toString() {
        return entry.getValue();
    }

    @Override
    boolean isSame(Operand operand) {
        if (!(operand instanceof ConstantStringOperand)) return false;
        else return getValue().equals(((ConstantStringOperand) operand).getValue());
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public ImmediateValue getImmediateValue() {
        return new ImmediateValue(entry.getSymbol());
    }
}
