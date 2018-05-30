package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;

import java.util.Arrays;
import java.util.List;

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
}
