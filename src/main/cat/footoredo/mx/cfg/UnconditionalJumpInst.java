package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Variable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UnconditionalJumpInst extends JumpInst {
    private Label target;

    public UnconditionalJumpInst(Label target) {
        this.target = target;
    }

    public Label getTarget() {
        return target;
    }

    public void setTarget(Label target) {
        this.target = target;
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public List<Label> getOutputs() {
        return Arrays.asList(target);
    }

    @Override
    public Set<Variable> backPropagate(Set<Variable> liveVariables) {
        return liveVariables;
    }

    @Override
    public void updateUsedCount() {
    }

    @Override
    public JumpInst copy() {
        return new UnconditionalJumpInst(target);
    }
}
