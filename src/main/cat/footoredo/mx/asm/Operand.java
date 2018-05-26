package cat.footoredo.mx.asm;

abstract public class Operand implements OperandPattern {
    abstract public String toSource(SymbolTable table);

    public boolean isRegister () { return false; }
    public boolean isMemoryReference () { return false; }

    @Override
    public boolean match(Operand operand) {
        return equals(operand);
    }
}
