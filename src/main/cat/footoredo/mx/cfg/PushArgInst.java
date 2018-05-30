package cat.footoredo.mx.cfg;

import java.util.Arrays;

public class PushArgInst extends Instruction {
    private int index;

    public PushArgInst (Operand source, int index) {
        super (Arrays.asList(source));
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void accept(CFGVisitor visitor) {
    }
}
