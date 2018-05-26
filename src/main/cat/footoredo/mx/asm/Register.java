package cat.footoredo.mx.asm;

abstract public class Register extends Operand {
    @Override
    public boolean isRegister() {
        return true;
    }
}
