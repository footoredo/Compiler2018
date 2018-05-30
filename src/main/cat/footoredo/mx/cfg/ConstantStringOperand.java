package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Symbol;
import cat.footoredo.mx.asm.Type;

public class ConstantStringOperand extends Operand {
    Symbol symbol;

    public ConstantStringOperand(Symbol symbol) {
        super (Type.INT64);
        this.symbol = symbol;
    }

    public Symbol getEntry() {
        return symbol;
    }

    public boolean isMemory () {
        return true;
    }
}
