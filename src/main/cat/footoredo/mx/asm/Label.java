package cat.footoredo.mx.asm;

public class Label extends Assembly {
    private Symbol symbol;

    public Label(Symbol symbol) {
        if (symbol == null) {
            throw new Error("fuck");
        }
        this.symbol = symbol;
    }

    public Label() {
        this (new UnnamedSymbol());
    }

    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return hashCode() == o.hashCode();
    }

    @Override
    public boolean isLabel() {
        return true;
    }

    @Override
    public String toSource(SymbolTable symbolTable) {
        return symbol.toSource(symbolTable) + ":";
    }
}
