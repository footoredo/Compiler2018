package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {
    private Label label;
    private List<Instruction> instructions;
    private List<BasicBlock> inputs;
    private JumpInst jumpInst;

    public BasicBlock(Label label) {
        this.label = label;
        this.instructions = new ArrayList<>();
        this.inputs = new ArrayList<>();
    }

    public void addInstruction (Instruction instruction) {
        instructions.add(instruction);
    }

    public void addInput (BasicBlock basicBlock) {
        inputs.add(basicBlock);
    }

    public JumpInst getJumpInst() {
        return jumpInst;
    }

    public void setJumpInst(JumpInst jumpInst) {
        this.jumpInst = jumpInst;
    }

    public Label getLabel() {
        return label;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
