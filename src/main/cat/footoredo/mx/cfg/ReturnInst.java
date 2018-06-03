package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.Variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReturnInst extends Instruction {
    public ReturnInst (Operand value) {
        super (Arrays.asList(value));
    }

    @Override
    public String toString() {
        if (hasValue()) {
            return "Return " + getValue();
        }
        else {
            return "Return";
        }
    }

    @Override
    public Instruction copy() {
        if (hasValue())
            return new ReturnInst(getValue().copy());
        else
            return new ReturnInst();
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
    public void resetLive() {
        super.setLive(true);
    }

    @Override
    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        Set<Variable> resultLiveVariables = new HashSet<>(liveVariables);
        // System.out.println (hasValue() + " " + getValue());
        if (hasValue() && getValue().isVariable()) {
            // System.out.println ("ss");
            // getValue().getVariable().setUsed(true);
            resultLiveVariables.add(getValue().getVariable());
        }
        // System.out.println("asfas");
        // System.out.println (resultLiveVariables.size());
        return resultLiveVariables;
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
