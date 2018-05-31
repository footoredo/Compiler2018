package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.Variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        Set<Variable> resultLiveVariables = new HashSet<>(liveVariables);
        if (hasValue() && getValue().isVariable()) {
            getValue().getVariable().setUsed(true);
            resultLiveVariables.add(getValue().getVariable());
        }
        // System.out.println("asfas");
        setLive(true);
        // System.out.println (resultLiveVariables.size());
        return resultLiveVariables;
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
