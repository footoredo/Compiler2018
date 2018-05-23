package cat.footoredo.mx.asm;

public class Operand implements OperandPattern {

    @Override
    public boolean match(Operand operand) {
        return equals(operand);
    }
}
