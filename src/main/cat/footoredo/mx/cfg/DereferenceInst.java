package cat.footoredo.mx.cfg;

import java.util.Arrays;

public class DereferenceInst extends Instruction {
    public DereferenceInst (Operand result, Operand address) {
        super (result, Arrays.asList(address));
    }

    public Operand getAddress () {
        return getOperand(0);
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
