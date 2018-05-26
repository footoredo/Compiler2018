package cat.footoredo.mx.asm;

public class UnnamedSymbol extends BaseSymbol {
    @Override
    public String toSource() {
        throw new Error("UnnamedSymbol#toSource() called");
    }

    @Override
    public String toSource(SymbolTable symbolTable) {
        return symbolTable.symbolString(this);
    }

    public String getName () {
        throw new Error("unnamed symbol");
    }

    public String toString () {
        return super.toString();
    }

    @Override
    public int cmp(IntegerLiteral i) {
        return 1;
    }

    @Override
    public int cmp(NamedSymbol symbol) {
        return 1;
    }

    @Override
    public int cmp(UnnamedSymbol symbol) {
        return toString().compareTo(symbol.toString());
    }

    @Override
    public int compareTo(Literal literal) {
        return -(literal.compareTo(this));
    }
}
