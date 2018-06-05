package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.Scope;
import cat.footoredo.mx.entity.Variable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract public class Instruction {
    private Operand result;
    private List<Operand> operands;
    private boolean isLive;
    private Set<Variable> liveVariables;    // what is needed after the execution of this command

    public Instruction(Operand result, List<Operand> operands) {
        this.result = result;
        this.operands = operands;
        this.isLive = false;
    }

    public boolean isMemorable () {
        if (result != null && !result.isMemorable())
            return false;
        else
            return true;
    }

    public void resetLive () {
        isLive = false;
    }

    public Set<Variable> getLiveVariables() {
        return liveVariables;
    }

    public Set<Variable> getAffectedVariables () {
        Set <Variable> results = new HashSet<>();
        if (result != null && result.isVariable()) {
            results.add (result.getVariable());
        }
        return results;
    }

    public void setLiveVariables(Set<Variable> liveVariables) {
        this.liveVariables = liveVariables;
    }

    abstract public Instruction copy();

    public void replace (Map<Variable, Variable> replacement, Scope scope) {
        if (result != null) {
            result.replace (replacement, scope);
        }

        for (Operand operand: operands) {
            // System.out.println ("replacing " + operand);
            operand.replace(replacement, scope);
        }
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

    public boolean isCallLike () {
        return false;
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
                    if (variable == null)
                        throw new Error ("ss");
                    // variable.setUsed(true);
                    resultLiveVariables.add(variable);
                }
            resultLiveVariables.remove(result.getVariable());
            RegisterAllocator.solveRivalry(resultLiveVariables);
        }
        /*System.err.println (getClass() + " : after");
        for (Variable variable: resultLiveVariables)
            System.err.println (variable.getName());*/
        return resultLiveVariables;
    }

    public boolean affect (Variable variable) {
        return result != null && result.isVariable() && result.getVariable() == variable;
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
