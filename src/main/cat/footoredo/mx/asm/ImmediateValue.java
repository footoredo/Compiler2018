package cat.footoredo.mx.asm;

public class ImmediateValue extends Operand {
    private Literal value;

    public ImmediateValue(Literal value) {
        if (value == null)
            throw new Error ("sada");
        this.value = value;
    }

    public ImmediateValue(long value) {
        this (new IntegerLiteral(value));
    }

    public boolean equals (Object other) {
        if (!(other instanceof ImmediateValue)) return false;
        return value.equals(((ImmediateValue) other).value);
    }

    @Override
    public int hashCode() {
        return ("IV" + value.hashCode()).hashCode();
    }

    public Literal getValue() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public boolean isConstantInteger() {
        return value instanceof IntegerLiteral;
    }

    public long getIntegerValue () {
        return ((IntegerLiteral) value).getValue();
    }

    @Override
    public String toSource(SymbolTable table) {
        return value.toSource(table);
    }
}
