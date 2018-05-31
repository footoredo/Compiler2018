package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.utils.ListUtils;

import java.util.*;

public class BasicBlock {
    private Label label;
    private List<Instruction> instructions;
    private Set<BasicBlock> inputs;
    private Set<BasicBlock> outputs;
    private JumpInst jumpInst;
    private boolean isEndBlock;
    private Set<Variable> liveVariables;

    public BasicBlock(Label label) {
        this.label = label;
        this.instructions = new ArrayList<>();
        this.inputs = new HashSet<>();
        this.outputs = new HashSet<>();
        this.isEndBlock = false;
        this.liveVariables = null;
    }

    public void cleanInstructions () {
        List<Instruction> newInstructions = new ArrayList<>();

        boolean verbose = false;
        if (verbose) {
            for (Variable variable: liveVariables) {
                System.err.println (variable.getName());
            }
        }

        if (verbose)
            System.err.println ("before clean: ");
        for (Instruction instruction: instructions) {
            if (verbose)
                System.err.println (instruction.getClass());
            if (instruction.isLive()) {
                newInstructions.add(instruction);
            }
        }

        instructions = newInstructions;

        if (verbose) {
            System.err.println("\nafter clean: ");
            for (Instruction instruction : instructions) {
                System.err.println(instruction.getClass());
            }
        }
    }

    public boolean backPropageted () {
        return liveVariables != null;
    }

    public void prepareForFirstBackPropagate () {
        liveVariables = new HashSet<>();
    }

    public Set<Variable> getLiveVariables() {
        return liveVariables;
    }

    public boolean isLiveVariable (Variable variable) {
        return liveVariables.contains(variable);
    }

    public void addLiveVariable (Variable variable) {
        liveVariables.add (variable);
    }

    public boolean isEndBlock() {
        return isEndBlock;
    }

    public void addInstruction (Instruction instruction) {
        if (instruction instanceof ULTIMATERETURNINST) {
            isEndBlock = true;
        }
        instructions.add(instruction);
    }

    public void addInput (BasicBlock basicBlock) {
        inputs.add(basicBlock);
    }

    public int getIntputCount () {
        return inputs.size();
    }

    public void addOutput (BasicBlock basicBlock) {
        outputs.add (basicBlock);
    }

    public int getOutputCount () {
        return outputs.size();
    }

    public Set<BasicBlock> getInputs() {
        return inputs;
    }

    public Set<BasicBlock> getOutputs() {
        return outputs;
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

    public List<Label> getOutputLabels () {
        if (jumpInst == null)
            return Arrays.asList();
        else
            return jumpInst.getOutputs();
    }

    public Set<Variable> backPropagate () {
        Set<Variable> result = new HashSet<>(liveVariables);
        if (jumpInst != null)
            result = jumpInst.backPropagate(result);
        for (Instruction instruction: ListUtils.reverse(instructions)) {
            result = instruction.backPropagate (result);
        }
        ListUtils.reverse(instructions);
        return result;
    }
}
