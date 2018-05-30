package cat.footoredo.mx.cfg;

import cat.footoredo.mx.ir.Op;

import java.util.Arrays;

public class CompareInst extends Instruction {
    private Op op;

    public CompareInst(Operand result, Operand a, Operand b, Op op) {
        super(result, Arrays.asList(a, b));
        this.op = op;
    }

    public Op getOp() {
        return op;
    }

    @Override
    public void accept(CFGVisitor visitor) {
    }
}
