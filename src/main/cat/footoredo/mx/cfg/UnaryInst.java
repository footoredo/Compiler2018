package cat.footoredo.mx.cfg;

import cat.footoredo.mx.ir.Op;

import java.util.Arrays;

public class UnaryInst extends Instruction {
    private Op op;

    public UnaryInst(Operand result, Op op, Operand operand) {
        super (result, Arrays.asList(operand));
        this.op = op;
    }

    @Override
    public Instruction copy() {
        return new UnaryInst(getResult().copy(), op, getOperand().copy());
    }

    public Op getOp() {
        return op;
    }

    public Operand getOperand () {
        return super.getOperand(0);
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
