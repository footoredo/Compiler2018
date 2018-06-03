package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.DefinedFunction;
import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.Variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallInst extends Instruction {
    private Function function;

    public CallInst (Operand result, Function function, List<Operand> operands) {
        super (result, operands);
        this.function = function;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Call " + getResult() + " = " + function.getName() + "(");
        String sep = "";
        for (Operand operand: getOperands()) {
            buffer.append(sep);
            sep = ", ";
            buffer.append (operand);
        }
        buffer.append (")");
        return buffer.toString();
    }

    @Override
    public Instruction copy() {
        List<Operand> copiedOperands = new ArrayList<>();
        for (Operand operand: getOperands())
            copiedOperands.add (operand.copy());
        return new CallInst(hasResult() ? getResult().copy() : null, function, copiedOperands);
    }

    public boolean hasResult () {
        return getResult() != null;
    }

    public Function getFunction() {
        return function;
    }

    public int getArgc () {
        return getOperandsLength();
    }

    public Operand getArg (int i) {
        return getOperand(i);
    }

    @Override
    public void resetLive() {
        super.setLive(true);
    }

    @Override
    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        Set<Variable> resultLiveVariables = new HashSet<>(liveVariables);
        for (Operand arg: getOperands()) {
            if (arg.isVariable()) {
                // arg.getVariable().setUsed(true);
                resultLiveVariables.add(arg.getVariable());
            }
        }
        if (getResult() != null) {
            resultLiveVariables.add(getResult().getVariable());
        }
        RegisterAllocator.solveRivalry(resultLiveVariables);
        if (getResult() != null && getResult().isVariable()) {
            Variable result = getResult().getVariable();
            if (resultLiveVariables.contains(result))
                resultLiveVariables.remove(result);
        }
        return resultLiveVariables;
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
