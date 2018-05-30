package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;

import java.util.Arrays;
import java.util.List;

public class UnconditionalJumpInst extends JumpInst {
    private Label target;

    public UnconditionalJumpInst(Label target) {
        this.target = target;
    }

    public Label getTarget() {
        return target;
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public List<Label> getOutputs() {
        return Arrays.asList(target);
    }
}
