package cat.footoredo.mx.asm;

public class NamedSymbol extends BaseSymbol {
    private String name;

    public NamedSymbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toSource() {
        return name;
    }

    @Override
    public String toSource(SymbolTable symbolTable) {
        return name;
    }

    public String toString () {
        return "#" + name;
    }

    @Override
    public int cmp(IntegerLiteral i) {
        return 1;
    }

    @Override
    public int cmp(NamedSymbol symbol) {
        return name.compareTo(symbol.name);
    }

    @Override
    public int cmp(UnnamedSymbol symbol) {
        return -1;
    }

    @Override
    public int compareTo(Literal literal) {
        return -(literal.compareTo(this));
    }
}
