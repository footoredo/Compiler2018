package cat.footoredo.mx.asm;

public interface Literal extends Comparable<Literal> {
    public String toSource ();
    public String toSource (SymbolTable symbolTable);
    public boolean isZero ();
    public Literal plus (long diff);
    public int cmp (IntegerLiteral i);
    public int cmp (NamedSymbol symbol);
    public int cmp (UnnamedSymbol symbol);
}
