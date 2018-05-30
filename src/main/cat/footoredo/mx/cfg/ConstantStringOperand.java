package cat.footoredo.mx.cfg;

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

    public boolean isMemory () {
        return true;
    }

    @Override
    public MemoryReference getMemoryReference() {
        return entry.getMemoryReference();
    }
}
