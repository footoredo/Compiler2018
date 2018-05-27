package cat.footoredo.mx.asm;

public class IntegerLiteral implements Literal {
    private long value;

    public IntegerLiteral(long value) {
        this.value = value;
    }

    public boolean equals (Object other) {
        return (other instanceof IntegerLiteral && equals((IntegerLiteral) other));
    }

    public boolean equals (IntegerLiteral other) {
        return other.value == this.value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean isZero () {
        return value == 0;
    }

    @Override
    public IntegerLiteral plus (long diff) {
        return new IntegerLiteral(value + diff);
    }

    @Override
    public String toSource() {
        return Long.toString(value);
    }

    @Override
    public String toSource(SymbolTable symbolTable) {
        return toSource();
    }

    @Override
    public int compareTo(Literal literal) {
        return -(literal.cmp(this));
    }

    @Override
    public int cmp(IntegerLiteral i) {
        return Long.compare(this.value, i.value);
    }

    @Override
    public int cmp(NamedSymbol symbol) {
        return -1;
    }

    @Override
    public int cmp(UnnamedSymbol symbol) {
        return -1;
    }

    @Override
    public void collectStatistics(Statistics statistics) {

    }
}
