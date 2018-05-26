package cat.footoredo.mx.asm;

public class Label extends Assembly {
    private Symbol symbol;

    public Label(Symbol symbol) {
        this.symbol = symbol;
    }

    public Label() {
        this (new UnnamedSymbol());
    }

    public Symbol getSymbol() {
        return symbol;
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
