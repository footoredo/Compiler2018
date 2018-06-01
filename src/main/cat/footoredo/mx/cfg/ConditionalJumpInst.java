package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Scope;
import cat.footoredo.mx.entity.Variable;

import java.util.*;

public class ConditionalJumpInst extends JumpInst {
    private Operand condition;
    private Label trueTarget, falseTarget;

    public ConditionalJumpInst(Operand condition, Label trueTarget, Label falseTarget) {
        this.condition = condition;
        this.trueTarget = trueTarget;
        this.falseTarget = falseTarget;
    }

    @Override
    public JumpInst copy() {
        return new ConditionalJumpInst(condition.copy(), trueTarget, falseTarget);
    }

    @Override
    public void replace (Map<Variable, Variable> replacement, Scope scope) {
        condition.replace(replacement, scope);
    }

    public void setTrueTarget(Label trueTarget) {
        this.trueTarget = trueTarget;
    }

    public void setFalseTarget(Label falseTarget) {
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
