package cat.footoredo.mx.cfg;

import cat.footoredo.mx.ir.Op;

import java.util.Arrays;

public class AssignInst extends Instruction {
    private boolean isDeref;
    public AssignInst(Operand a, Operand b, boolean isDeref) {
        super(Arrays.asList(a, b));
        this.isDeref = isDeref;
    }

    public boolean isDeref() {
        return isDeref;
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