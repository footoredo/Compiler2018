package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.ir.Binary;
import cat.footoredo.mx.utils.ListUtils;
import cat.footoredo.mx.utils.SetUtils;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.*;

public class BasicBlock {
    private Label label;
    private List<Instruction> instructions;
    private Set<BasicBlock> inputs;
    private Set<BasicBlock> outputs;
    private BasicBlock backOutput;
    private JumpInst jumpInst;
    private boolean isEndBlock;
    private boolean hasCall;
    private List<Set<Variable>> liveVariables;
    private List<BasicBlock> belongedLoopHeaders;
    private BasicBlock loopEnd;
    private Set<Variable> loopVariants;
    private boolean loopHasCall;

    public BasicBlock(Label label) {
        this.label = label;
        this.instructions = new ArrayList<>();
        this.inputs = new HashSet<>();
        this.outputs = new HashSet<>();
        this.isEndBlock = false;
        this.liveVariables = null;
        this.belongedLoopHeaders = new ArrayList<>();
        this.loopEnd = null;
        this.loopVariants = null;
        this.backOutput = null;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public boolean hasBackOutput () {
        return backOutput != null;
    }

    public BasicBlock getBackOutput() {
        return backOutput;
    }

    public void setBackOutput(BasicBlock backOutput) {
        this.backOutput = backOutput;
    }

    public int getAllInstructionCount () {
        return instructions.size() + (jumpInst == null ? 0 : 1) + 1;
    }

    public boolean cleanInstructions () {
        // displayInstructions();

        /*for (Variable variable: liveVariables) {
            System.out.println ("LIVE: " + variable.getName());
        }*/

        boolean cleaned = false;

        List<Instruction> newInstructions = new ArrayList<>();

        boolean verbose = false;
        /*if (verbose) {
            for (Variable variable: liveVariables) {
                System.err.println (variable.getName());
            }
        }*/

        if (verbose) {
            System.err.println("before clean: ");
            for (Instruction instruction : instructions) {
                System.err.println(instruction);
            }
        }
        for (Instruction instruction: instructions) {
            if (instruction.isLive()) {
                // instruction.updateUsedCount ();
                newInstructions.add(instruction);
            }
            else {
                cleaned = true;
            }
        }

        instructions = newInstructions;

        hasCall = false;
        for (Instruction instruction: instructions)
            if (instruction.isCallLike())
                hasCall = true;

        if (verbose) {
            System.err.println("\nafter clean: ");
            for (Instruction instruction : instructions) {
                System.err.println(instruction);
            }
        }

        return cleaned;

        // displayInstructions();
    }

    public void resetLive () {
        for (Instruction instruction: instructions) {
            instruction.resetLive ();
        }
    }

    public BasicBlock getLoopEnd() {
        return loopEnd;
    }

    public void setLoopEnd(BasicBlock loopEnd) {
        this.loopEnd = loopEnd;
    }

    public boolean isLoopHeader () {
        return loopEnd != null;
    }

    public void resetLoop () {
        belongedLoopHeaders = new ArrayList<>();
        loopEnd = null;
        loopHasCall = false;
        loopVariants = new HashSet<>();
    }

    public void addLoopVariants (Set<Variable> loopVariants) {
        this.loopVariants.addAll(loopVariants);
    }

    public void setLoopHasCall (boolean loopHasCall) {
        this.loopHasCall |= loopHasCall;
    }

    public boolean removeLoop () {
        assert (isLoopHeader());
        //System.out.println ("loop removed");
        // System.err.println (loopHasCall);
        if (!loopHasCall) {
            /*System.err.println ("==== loop variants ====");
            for (Variable variable: loopVariants) {
                System.err.println (variable.getName());
            }
            System.err.println();
            System.err.println ("==== loop end live variables ====");
            for (Variable variable: loopEnd.getEndLiveVariables()) {
                System.err.println (variable.getName());
            }
            System.err.println();
            System.err.println();*/
            if (SetUtils.solveIntersection(loopVariants, loopEnd.getEndLiveVariables()).isEmpty()) {
                System.err.println ("** loop removed **");
                // displayInstructions();
                instructions = new ArrayList<>();
                jumpInst = new UnconditionalJumpInst(loopEnd.getLabel());
                return true;
            }
        }
        return false;
    }

    public void addBelongedLoopHeader (BasicBlock loopHeader) {
        belongedLoopHeaders.add(loopHeader);
    }

    public List<BasicBlock> getBelongedLoopHeaders() {
        return belongedLoopHeaders;
    }

    public boolean remove () {
        /*boolean removed = false;
        int n = instructions.size();
        while (true) {
            boolean found = false;
            for (int i = 0; i < n - 1; ++ i) {
                // System.out.println ("checking " + i);
                if (instructions.get(i) instanceof AssignInst && instructions.get(i).isLive()) {
                    AssignInst assignInst = (AssignInst) instructions.get(i);
                    List<Variable> toCheck = new ArrayList<>();
                    if (assignInst.getLeft().isVariable())
                        toCheck.add (assignInst.getLeft().getVariable());
                    if (assignInst.getRight().isVariable())
                        toCheck.add (assignInst.getRight().getVariable());
                    // System.out.println ("i: " + i + " " + instructions.get(i));
                    for (int j = i + 1; j < n; ++ j) {
                        boolean affected = false;
                        // System.out.println (instructions.get(j));
                        if (instructions.get(j) instanceof AssignInst && instructions.get(j).isLive()) {
                            // System.out.println ("ere");
                            AssignInst otherAssignInst = (AssignInst) instructions.get(j);
                            if (assignInst.getLeft() == otherAssignInst.getRight()) {
                                // System.out.println ("FOUND! " + i + " " + j);
                                found = true;
                                instructions.set(i, new AssignInst(otherAssignInst.getLeft(), assignInst.getRight(), otherAssignInst.isDeref()));
                                otherAssignInst.setLive(false);
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
            removed |= found;
            if (!found) break;
        }
        if (removed) {
            System.out.println ("sss");
        }
        return removed;*/
        return false;
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
        this.hasCall |= basicBlock.hasCall;
        this.liveVariables = null;
        this.backOutput = basicBlock.backOutput;
        this.jumpInst = basicBlock.jumpInst;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = new ArrayList<>();
        this.isEndBlock = false;
        this.hasCall = false;
        for (Instruction instruction: instructions)
            addInstruction(instruction);
    }

    public void resetLiveVariables() {
        this.liveVariables = null;
    }

    public boolean backPropagated () {
        return liveVariables != null;
    }

    public void prepareForFirstBackPropagate () {
        liveVariables = new ArrayList<>(getAllInstructionCount());
        for (int i = 0; i < getAllInstructionCount() - 1; ++ i)
            liveVariables.add(null);
        // System.out.println(getAllInstructionCount());
        liveVariables.add(new HashSet<>());
    }

    public Set<Variable> getLiveVariables() {
        return liveVariables.get(getAllInstructionCount() - 1);
    }

    public boolean isLiveVariable (Variable variable) {
        if (getLiveVariables().contains(variable)) {
            return true;
        }
        return false;
    }

    public void addLiveVariable (Variable variable) {
        getLiveVariables().add (variable);
    }

    public boolean isEndBlock() {
        return isEndBlock;
    }

    public void addInstruction (Instruction instruction) {
        if (instruction instanceof ULTIMATERETURNINST) {
            // System.out.println (this + " is end block " + "with label " + label);
            isEndBlock = true;
        }
        else if (instruction.isCallLike()) {
            // System.out.println ("sss");
            hasCall = true;
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

    public Set<BasicBlock> getAllOutputs() {
        Set<BasicBlock> result = new HashSet<>(outputs);
        if (backOutput != null) {
            result.add(backOutput);
        }
        return result;
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

    public void resetInputs () {
        inputs = new HashSet<>();
    }

    public void resetOutputs () {
        outputs = new HashSet<>();
        backOutput = null;
    }

    public Label getEndLabel () {
        return label.getPairedEndLabel();
    }

    public Set<Variable> backPropagate () {
        // System.out.println ("sd");
        Set<Variable> result = new HashSet<>(getLiveVariables());
        // RegisterAllocator.solveRivalry(result);
        int countDown = getAllInstructionCount() - 1;
        countDown --;
        if (jumpInst != null) {
            result = jumpInst.backPropagate(result);
            liveVariables.set(countDown --, new HashSet<>(result));
            // RegisterAllocator.solveRivalry(result);
        }
        for (Instruction instruction: ListUtils.reverse(instructions)) {
            instruction.setLiveVariables(new HashSet<>(result));
            result = instruction.backPropagate (result);
            /*for (Variable variable: result)
                System.out.println (variable.getName());*/
            liveVariables.set(countDown --, new HashSet<>(result));
        }
        assert (countDown == -1);
        ListUtils.reverse(instructions);
        return result;
    }

    public Set<Variable> getAffectedVariables () {
        Set<Variable> results = new HashSet<>();
        for (Instruction instruction: instructions) {
            results.addAll(instruction.getAffectedVariables());
        }
        return results;
    }

    public boolean hasCall () {
        return hasCall;
    }

    public void solveRivalry () {
        for (Set<Variable> live: liveVariables) {
            RegisterAllocator.solveRivalry(live);
        }
    }

    public void solveUsedCount () {
        for (Instruction instruction: instructions) {
            instruction.updateUsedCount();
        }
        if (jumpInst != null)
            jumpInst.updateUsedCount();
    }

    public Set<Variable> getEndLiveVariables() {
        return liveVariables.get(0);
    }
}
