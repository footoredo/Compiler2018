package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.ir.Binary;
import cat.footoredo.mx.utils.ListUtils;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.*;

public class BasicBlock {
    private Label label;
    private List<Instruction> instructions;
    private Set<BasicBlock> inputs;
    private Set<BasicBlock> outputs;
    private JumpInst jumpInst;
    private boolean isEndBlock;
    private Set<Variable> liveVariables;
    private Set<Variable> endLiveVariables;

    public BasicBlock(Label label) {
        this.label = label;
        this.instructions = new ArrayList<>();
        this.inputs = new HashSet<>();
        this.outputs = new HashSet<>();
        this.isEndBlock = false;
        this.liveVariables = null;
    }

    public void cleanInstructions () {
        // displayInstructions();

        /*for (Variable variable: liveVariables) {
            System.out.println ("LIVE: " + variable.getName());
        }*/

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
                instruction.updateUsedCount ();
                newInstructions.add(instruction);
            }
        }

        if (jumpInst != null)
            jumpInst.updateUsedCount ();

        instructions = newInstructions;

        if (verbose) {
            System.err.println("\nafter clean: ");
            for (Instruction instruction : instructions) {
                System.err.println(instruction.getClass());
            }
        }

        // displayInstructions();
    }

    public void CSE () {
        int n = instructions.size();
        // System.out.println (n);
        while (true) {
            boolean found = false;
            for (int i = 0; i < n - 1; ++ i) {
                // System.out.println ("checking " + i);
                if (instructions.get(i) instanceof BinaryInst) {
                    BinaryInst binaryInst = (BinaryInst) instructions.get(i);
                    List<Variable> toCheck = new ArrayList<>();
                    if (binaryInst.getLeft().isVariable())
                        toCheck.add (binaryInst.getLeft().getVariable());
                    if (binaryInst.getRight().isVariable())
                        toCheck.add (binaryInst.getRight().getVariable());
                    if (binaryInst.getResult().isVariable())
                        toCheck.add (binaryInst.getResult().getVariable());
                    // System.out.println ("i: " + i + " " + instructions.get(i));
                    for (int j = i + 1; j < n; ++ j) {
                        boolean affected = false;
                        // System.out.println (instructions.get(j));
                        if (j > i && instructions.get(j) instanceof BinaryInst) {
                            // System.out.println ("ere");
                            BinaryInst otherBinaryInst = (BinaryInst) instructions.get(j);
                            if (binaryInst.isSame(otherBinaryInst)) {
                                // System.out.println ("FOUND! " + i + " " + j);
                                found = true;
                                instructions.set(j, new AssignInst(otherBinaryInst.getResult(), binaryInst.getResult(), false));
                                break;
                            }
                        }
                        for (Variable variable: toCheck) {
                            if (instructions.get(j).affect(variable)) {
                                affected = true;
                                break;
                            }
                        }
                        if (affected) break;
                    }
                    if (found) {
                        // System.out.println ("gonna break");
                        break;
                    }
                }
            }
            if (!found) break;
        }
    }

    public void displayInstructions () {
        System.out.println ("\n\n-- Instructions of " + toString() + " --\n");
        for (Instruction instruction: instructions) {
            System.out.println (instruction);
        }
    }

    public void merge (BasicBlock basicBlock) {
        this.instructions.addAll (basicBlock.getInstructions());
        this.outputs = basicBlock.outputs;
        this.isEndBlock = basicBlock.isEndBlock;
        this.liveVariables = basicBlock.liveVariables;
        this.jumpInst = basicBlock.jumpInst;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public void resetLiveVariables() {
        liveVariables = new HashSet<>();
    }

    public boolean backPropagated () {
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
            // System.out.println (this + " is end block " + "with label " + label);
            isEndBlock = true;
        }
        instructions.add(instruction);
    }

    public void setEndBlock(boolean endBlock) {
        isEndBlock = endBlock;
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
        RegisterAllocator.solveRivalry(result);
        if (jumpInst != null) {
            result = jumpInst.backPropagate(result);
            RegisterAllocator.solveRivalry(result);
        }
        for (Instruction instruction: ListUtils.reverse(instructions)) {
            result = instruction.backPropagate (result);
        }
        ListUtils.reverse(instructions);
        this.endLiveVariables = result;
        return result;
    }

    public Set<Variable> getEndLiveVariables() {
        return endLiveVariables;
    }
}
