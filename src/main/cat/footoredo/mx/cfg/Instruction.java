package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.Variable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract public class Instruction {
    private Operand result;
    private List<Operand> operands;
    private boolean isLive;

    public Instruction(Operand result, List<Operand> operands) {
        this.result = result;
        this.operands = operands;
        this.isLive = false;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    boolean liveCheck (Set<Variable> liveVariables) {
        return result != null && result.isVariable() && (liveVariables.contains(result.getVariable()) || result.getVariable().isGlobal());
    }

    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        Set<Variable> resultLiveVariables = new HashSet<>(liveVariables);
        /*System.err.println (getClass() + " : before");
        for (Variable variable: resultLiveVariables)
            System.err.println (variable.getName());*/
        if (liveCheck (liveVariables)) {
            // System.out.println ("here");
            setLive(true);
            for (Operand operand: operands)
                if (operand.isVariable()) {
                    Variable variable = operand.getVariable();
                    // variable.setUsed(true);
                    resultLiveVariables.add(variable);
                }
            RegisterAllocator.solveRivalry(resultLiveVariables);
            resultLiveVariables.remove(result.getVariable());
        }
        /*System.err.println (getClass() + " : after");
        for (Variable variable: resultLiveVariables)
            System.err.println (variable.getName());*/
        return resultLiveVariables;
    }

    public void updateUsedCount () {
        if (result != null && result.isVariable())
            result.getVariable().addUsedCount();

        for (Operand operand: operands)
            if (operand.isVariable())
                operand.getVariable().addUsedCount();;
    }

    public Instruction(List<Operand> operands) {
        this (null, operands);
    }

    public int getOperandsLength() {
        return operands.size();
    }

    public List<Operand> getOperands() {
        return operands;
    }

    public Operand getOperand (int i) {
        return operands.get(i);
    }

    public Operand getResult() {
        return result;
    }

    abstract public void accept (CFGVisitor visitor);
}
