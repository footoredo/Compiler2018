package cat.footoredo.mx.asm;

public class ImmediateValue extends Operand {
    private Literal value;

    public ImmediateValue(Literal value) {
        this.value = value;
    }

    public ImmediateValue(long value) {
        this (new IntegerLiteral(value));
    }

    public boolean equals (Object other) {
        if (!(other instanceof ImmediateValue)) return false;
        return value.equals(((ImmediateValue) other).value);
    }

    public Literal getValue() {
        return value;
    }

    @Override
    public String toSource(SymbolTable table) {
        return value.toSource(table);
    }
}
