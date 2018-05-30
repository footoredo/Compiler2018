package cat.footoredo.mx.cfg;

import cat.footoredo.mx.ir.Op;

import java.util.Arrays;

public class AssignInst extends Instruction {
    public AssignInst(Operand a, Operand b) {
        super(Arrays.asList(a, b));
    }

    public Operand getLeft () {
        return getOperand(0);
    }

    public Operand getRight () {
        return getOperand(1);
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}