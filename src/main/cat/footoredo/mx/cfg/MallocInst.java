package cat.footoredo.mx.cfg;

import cat.footoredo.mx.ir.Malloc;

import java.util.Arrays;

public class MallocInst extends Instruction {
    public MallocInst (Operand result, Operand length) {
        super (result, Arrays.asList(length));
        // System.out.println (length.getImmediateValue().getIntegerValue());
    }

    @Override
    public Instruction copy() {
        return new MallocInst(getResult().copy(), getLength().copy());
    }

    public Operand getLength () {
        return getOperand(0);
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
