package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConditionalJumpInst extends JumpInst {
    private Operand condition;
    private Label trueTarget, falseTarget;

    public ConditionalJumpInst(Operand condition, Label trueTarget, Label falseTarget) {
        this.condition = condition;
        this.trueTarget = trueTarget;
        this.falseTarget = falseTarget;
    }

    public Operand getCondition() {
        return condition;
    }

    public Label getTrueTarget() {
        return trueTarget;
    }

    public Label getFalseTarget() {
        return falseTarget;
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public List<Label> getOutputs() {
        return Arrays.asList(trueTarget, falseTarget);
    }

    @Override
    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        Set<Variable> resultLiveVariables = new HashSet<>(liveVariables);
        if (condition.isVariable()) {
            // condition.getVariable().setUsed(true);
            resultLiveVariables.add (condition.getVariable());
        }
        return resultLiveVariables;
    }

    @Override
    public void updateUsedCount() {
        if (condition.isVariable())
            condition.getVariable().addUsedCount();
    }
}
