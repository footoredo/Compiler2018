package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.Variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DereferenceInst extends Instruction {
    public DereferenceInst (Operand result, Operand address) {
        super (result, Arrays.asList(address));
        // System.err.println ("Dereference: " + result.getVariable().getName() + " = *" + address.getVariable().getName());
    }

    @Override
    public Instruction copy() {
        return new DereferenceInst(getResult().copy(), getAddress().copy());
    }

    public Operand getAddress () {
        return getOperand(0);
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
/*
    @Override
    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        Set<Variable> resultLiveVariables = new HashSet<>(liveVariables);
        setLive(true);
        if (resultLiveVariables.con)
    }*/
}
