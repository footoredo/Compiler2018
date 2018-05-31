package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.Variable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CallInst extends Instruction {
    private Function function;

    public CallInst (Operand result, Function function, List<Operand> operands) {
        super (result, operands);
        this.function = function;
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
    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        Set<Variable> resultLiveVariables = new HashSet<>(liveVariables);
        if (getResult() != null && getResult().isVariable()) {
            Variable result = getResult().getVariable();
            if (resultLiveVariables.contains(result))
                resultLiveVariables.remove(result);
        }
        for (Operand arg: getOperands()) {
            if (arg.isVariable()) {
                // arg.getVariable().setUsed(true);
                resultLiveVariables.add(arg.getVariable());
            }
        }
        setLive(true);
        return resultLiveVariables;
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
