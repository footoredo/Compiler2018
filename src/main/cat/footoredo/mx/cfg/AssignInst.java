package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.ir.Op;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AssignInst extends Instruction {
    private boolean isDeref;
    public AssignInst(Operand a, Operand b, boolean isDeref) {
        super(a, Arrays.asList(b));
        this.isDeref = isDeref;
    }

    @Override
    public String toString() {
        return "Assign " + getLeft() + " = " + getRight();
    }

    @Override
    public Instruction copy() {
        return new AssignInst(getLeft().copy(), getRight().copy(), isDeref);
    }

    public boolean isDeref() {
        return isDeref;
    }

    public Operand getLeft () {
        return getResult();
    }

    public Operand getRight () {
        return getOperand(0);
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        Set<Variable> resultLiveVariables = new HashSet<>(liveVariables);
        // System.err.println (getClass() + " : before");
        /*for (Variable variable: resultLiveVariables)
            System.err.println (variable.getName());*/
        if (liveCheck(liveVariables) || isDeref) {
            // System.out.println ("here");
            setLive(true);
            if (isDeref) {
                resultLiveVariables.add (getResult().getVariable());
                if (getResult().getVariable() == null)
                    throw new Error ("ss");
            }
            for (Operand operand: getOperands())
                if (operand.isVariable()) {
                    Variable variable = operand.getVariable();
                    // variable.setUsed(true);
                    resultLiveVariables.add(variable);
                    if (variable == null)
                        throw new Error ("ss");
                }
            if (!isDeref) {
                resultLiveVariables.remove (getResult().getVariable());
            }
            RegisterAllocator.solveRivalry(resultLiveVariables);
        }
        /*System.err.println (getClass() + " : after");
        for (Variable variable: resultLiveVariables)
            System.err.println (variable.getName());*/
        return resultLiveVariables;
    }
}