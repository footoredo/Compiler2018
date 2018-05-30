package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.sysdep.x86_64.RegisterClass;

public class RegisterOperand extends Operand {
    private int index;

    public RegisterOperand(Type type, int index) {
        super (type);
        this.index = index;
    }

    public RegisterOperand(Type type, RegisterClass registerClass) {
        super (type);
        this.index = registerClass.getValue();
    }

    public int getIndex() {
        return index;
    }
}
