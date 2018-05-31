package cat.footoredo.mx.cfg;

import java.util.Arrays;

public class ULTIMATERETURNINST extends Instruction {
    public ULTIMATERETURNINST () {
        super (Arrays.asList());
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
