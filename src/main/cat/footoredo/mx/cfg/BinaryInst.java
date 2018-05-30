package cat.footoredo.mx.cfg;

import cat.footoredo.mx.ir.Op;

import java.util.Arrays;

public class BinaryInst extends Instruction {
    private Op op;
    public BinaryInst (Operand result, Operand left, Op op, Operand right) {
        super (result, Arrays.asList(left, right));
        this.op = op;
    }

    public Op getOp() {
        return op;
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
