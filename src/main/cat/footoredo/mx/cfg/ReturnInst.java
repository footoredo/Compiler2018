package cat.footoredo.mx.cfg;

import java.util.Arrays;

public class ReturnInst extends Instruction {
    public ReturnInst (Operand value) {
        super (Arrays.asList(value));
        if (value == null)
            throw new Error ("asdas");
    }

    public ReturnInst () {
        super (Arrays.asList());
    }

    public boolean hasValue () {
        return getOperandsLength() > 0;
    }

    public Operand getValue () {
        if (!hasValue())
            throw new Error ("Getting value from empty return statement");
        return getOperand(0);
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
