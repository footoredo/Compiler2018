package cat.footoredo.mx.asm;

abstract public class Assembly {
    public boolean isComment () { return false; }
    public boolean isDirective () { return false; }
    public boolean isInstruction () { return false; }
    public boolean isLabel () { return false; }

    abstract public String toSource (SymbolTable symbolTable);
}
