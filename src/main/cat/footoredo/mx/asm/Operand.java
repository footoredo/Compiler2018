package cat.footoredo.mx.asm;

abstract public class Operand implements OperandPattern {
    abstract public String toSource(SymbolTable table);

    public boolean isRegister () { return false; }
    public boolean isMemoryReference () { return false; }
    public boolean isConstant () { return false; }
    public boolean isConstantInteger () { return false; }

    public Symbol getSymbol () {
        throw new Error ("its not symbol!");
    }

    @Override
    public boolean match(Operand operand) {
        return equals(operand);
    }

    public void collectStatistics (Statistics statistics) {

    }
}
