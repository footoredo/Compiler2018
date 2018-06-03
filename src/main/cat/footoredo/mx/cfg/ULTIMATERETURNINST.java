package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.Variable;

import java.util.Arrays;
import java.util.Set;

public class ULTIMATERETURNINST extends Instruction {
    public ULTIMATERETURNINST () {
        super (Arrays.asList());
    }

    @Override
    public Instruction copy() {
        return new ULTIMATERETURNINST();
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void resetLive() {
        super.setLive(true);
    }

    @Override
    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        return liveVariables;
    }
}
