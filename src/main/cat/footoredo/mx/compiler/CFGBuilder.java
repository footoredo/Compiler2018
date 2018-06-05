package cat.footoredo.mx.compiler;

import cat.footoredo.mx.asm.*;
import cat.footoredo.mx.cfg.*;
import cat.footoredo.mx.cfg.Instruction;
import cat.footoredo.mx.cfg.Operand;
import cat.footoredo.mx.entity.BuiltinFunction;
import cat.footoredo.mx.entity.DefinedFunction;
import cat.footoredo.mx.entity.Parameter;
import cat.footoredo.mx.ir.*;
import cat.footoredo.mx.ir.Integer;
import cat.footoredo.mx.ir.String;
import cat.footoredo.mx.type.BooleanType;
import cat.footoredo.mx.type.IntegerType;
import cat.footoredo.mx.type.PointerType;
import cat.footoredo.mx.type.VoidType;

import java.util.*;

public class CFGBuilder implements IRVisitor<Void, Operand> {
    private CFG cfg;
    private IR ir;

    private boolean inlined;
    private boolean notMemorableSet;

    public CFG generateCFG (IR ir) {
        this.ir = ir;
        cfg = new CFG();

        for (DefinedFunction definedFunction: ir.getAllDefinedFunctions()) {
            currentFunction = definedFunction;
            processDefinedFunction (definedFunction);
        }

        for (DefinedFunction definedFunction: ir.getAllDefinedFunctions()) {
            currentFunction = definedFunction;

            visitedBasicBlocks = new HashSet<>();
            dfsAndResetInputOutput(definedFunction.getStartBasicBlock());

            visitedBasicBlocks = new HashSet<>();
            stack = new HashSet<>();
            dfsAndLink(definedFunction.getStartBasicBlock());
        }

        for (DefinedFunction definedFunction : ir.getAllDefinedFunctions()) {
            currentFunction = definedFunction;
            visitedBasicBlocks = new HashSet<>();
            definedFunction.resetCalls ();
        }

        for (DefinedFunction definedFunction : ir.getAllDefinedFunctions()) {
            currentFunction = definedFunction;
            visitedBasicBlocks = new HashSet<>();
            dfsAndBuildCallGraph(definedFunction.getStartBasicBlock());
        }

        while (true) {
            notMemorableSet = false;
            for (DefinedFunction definedFunction : ir.getAllDefinedFunctions()) {
                currentFunction = definedFunction;
                visitedBasicBlocks = new HashSet<>();
                dfsAndSetNotMemorable(definedFunction.getStartBasicBlock());
            }
            if (notMemorableSet)
                break;
        }

        for (DefinedFunction definedFunction: ir.getAllDefinedFunctions()) {
            currentFunction = definedFunction;
            visitedBasicBlocks = new HashSet<>();
            dfsAndCSE (definedFunction.getStartBasicBlock());
        }

        /*while (true) {
            loopRemoved = false;
            cleaned = false;
            merged = false;
            removed = false;
            for (DefinedFunction definedFunction : ir.getAllDefinedFunctions()) {
                currentFunction = definedFunction;

                visitedBasicBlocks = new HashSet<>();
                dfsAndResetInputOutput(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                stack = new HashSet<>();
                dfsAndLink(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndResetLive(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();

                dfsAndRemove (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                Set<cat.footoredo.mx.entity.Variable> startLiveVariables = new HashSet<>();
                if (definedFunction.isFibLike()) {
                    startLiveVariables.add (definedFunction.getToSave());
                }
                backPropagate(definedFunction.getEndBasicBlock(), startLiveVariables);

                visitedBasicBlocks = new HashSet<>();
                dfsAndResetLoop (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndFindLoopHeader (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndBuildLoop (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndFindLoopVariants (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndRemoveLoops (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndResetInputOutput(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                stack = new HashSet<>();
                dfsAndLink(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndClean(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                //dfsAndMerge(definedFunction.getStartBasicBlock());
            }
            // System.out.println (loopRemoved + " " + removed + " " + cleaned + " " + merged);
            if (!loopRemoved && !removed && !cleaned && !merged) break;
        }*/


        for (int i = 0; i < 3; ++ i) {
            inlined = false;

            for (DefinedFunction definedFunction : ir.getAllDefinedFunctions()) {
                visitedBasicBlocks = new HashSet<>();
                definedFunction.resetCalls ();
            }

            for (DefinedFunction definedFunction : ir.getAllDefinedFunctions()) {
                currentFunction = definedFunction;
                visitedBasicBlocks = new HashSet<>();
                dfsAndBuildCallGraph(definedFunction.getStartBasicBlock());
            }

            for (DefinedFunction definedFunction : ir.getAllDefinedFunctions()) {
                visitedBasicBlocks = new HashSet<>();
                currentFunction = definedFunction;
                dfsAndInline(definedFunction.getStartBasicBlock(), false);
            }

            if (!inlined) break;
        }

        for (DefinedFunction definedFunction: ir.getAllDefinedFunctions()) {
            // System.out.println (definedFunction.getName() + " " + definedFunction.isMemorable());
            if (definedFunction.isFibLike()) {
                System.err.println (definedFunction.getName() + ": i am fibbed!");

                definedFunction.setSolved((cat.footoredo.mx.entity.Variable) ir.getScope().get(definedFunction.getSolvedName()));
                definedFunction.setAnswer((cat.footoredo.mx.entity.Variable) ir.getScope().get(definedFunction.getAnswerName()));

                Operand solved = new VariableOperand(definedFunction.getSolved());
                Operand answer = new VariableOperand(definedFunction.getAnswer());

                BasicBlock originalStartBlock = definedFunction.getStartBasicBlock();

                BasicBlock check1 = new BasicBlock(originalStartBlock.getLabel());
                definedFunction.setStartBasicBlock(check1);
                cfg.put (check1.getLabel(), check1);

                Label functionStartLabel = new Label();
                originalStartBlock.setLabel (functionStartLabel);
                cfg.put (functionStartLabel, originalStartBlock);

                Label failLabel = new Label();
                BasicBlock failBlock = new BasicBlock(failLabel);
                cfg.put (failLabel, failBlock);
                failBlock.setJumpInst(new UnconditionalJumpInst(functionStartLabel));

                Label rangeFailLabel = new Label();
                BasicBlock rangeFailBlock = new BasicBlock(rangeFailLabel);
                cfg.put (rangeFailLabel, rangeFailBlock);
                rangeFailBlock.setJumpInst(new UnconditionalJumpInst(functionStartLabel));

                Label check2Label = new Label();
                BasicBlock check2 = new BasicBlock(check2Label);
                cfg.put (check2Label, check2);

                Label check3Label = new Label();
                BasicBlock check3 = new BasicBlock(check3Label);
                cfg.put (check3Label, check3);

                Label yeahLabel = new Label();
                BasicBlock yeah = new BasicBlock(yeahLabel);
                cfg.put (yeahLabel, yeah);

                cat.footoredo.mx.entity.Variable toSave = definedFunction.getScope().allocateTmpVariable(new IntegerType(true));
                definedFunction.setToSave (toSave);

                Operand n = new VariableOperand(definedFunction.getParameter(0));

                Operand c1 = new VariableOperand(definedFunction.getScope().allocateTmpVariable(new BooleanType()));
                check1.addInstruction(new BinaryInst(c1, n, Op.S_LT, new ConstantIntegerOperand(Type.INT64, 128)));
                check1.setJumpInst(new ConditionalJumpInst(c1, check2Label, rangeFailLabel));

                Operand c2 = new VariableOperand(definedFunction.getScope().allocateTmpVariable(new BooleanType()));
                check2.addInstruction(new BinaryInst(c2, n, Op.S_GTEQ, new ConstantIntegerOperand(Type.INT64, 0)));
                check2.setJumpInst(new ConditionalJumpInst(c2, check3Label, rangeFailLabel));

                Operand address = new VariableOperand(definedFunction.getScope().allocateTmpVariable(new PointerType()));
                check3.addInstruction(new BinaryInst(address, solved, Op.ADD, n));
                Operand c3 = new VariableOperand(definedFunction.getScope().allocateTmpVariable(new BooleanType()));
                // System.out.println (c3.getVariable().size());
                check3.addInstruction(new DereferenceInst(c3, address));
                check3.setJumpInst(new ConditionalJumpInst(c3, yeahLabel, failLabel));

                // System.out.println ("failBlock: " + failBlock);
                failBlock.addInstruction(new AssignInst(
                        new VariableOperand(toSave),
                        n, false));

                rangeFailBlock.addInstruction(new AssignInst(
                        new VariableOperand(toSave),
                        new ConstantIntegerOperand(Type.INT64, -1), false));

                yeah.addInstruction(new AssignInst(
                        new VariableOperand(toSave),
                        new ConstantIntegerOperand(Type.INT64, -1), false));
                Operand offset = new VariableOperand(definedFunction.getScope().allocateTmpVariable(new PointerType()));
                yeah.addInstruction(new BinaryInst(offset, n, Op.MUL, new ConstantIntegerOperand(Type.INT64, 8)));
                Operand address2 = new VariableOperand(definedFunction.getScope().allocateTmpVariable(new PointerType()));
                yeah.addInstruction(new BinaryInst(address2, answer, Op.ADD, offset));
                Operand answerN = new VariableOperand(definedFunction.getScope().allocateTmpVariable(definedFunction.getReturnType()));
                yeah.addInstruction(new DereferenceInst(answerN, address2));
                yeah.addInstruction(new ReturnInst(answerN));
                yeah.setJumpInst(new UnconditionalJumpInst(definedFunction.getFunctionEndLabel()));
            }
        }

        while (true) {
            loopRemoved = false;
            cleaned = false;
            merged = false;
            removed = false;
            for (DefinedFunction definedFunction : ir.getAllDefinedFunctions()) {
                currentFunction = definedFunction;

                visitedBasicBlocks = new HashSet<>();
                dfsAndResetInputOutput(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                stack = new HashSet<>();
                dfsAndLink(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndResetLive(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();

                //dfsAndRemove (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                Set<cat.footoredo.mx.entity.Variable> startLiveVariables = new HashSet<>();
                if (definedFunction.isFibLike()) {
                    startLiveVariables.add (definedFunction.getToSave());
                }
                backPropagate(definedFunction.getEndBasicBlock(), startLiveVariables);

                visitedBasicBlocks = new HashSet<>();
                dfsAndResetLoop (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndFindLoopHeader (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndBuildLoop (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndFindLoopVariants (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndRemoveLoops (definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndResetInputOutput(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                stack = new HashSet<>();
                dfsAndLink(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndClean(definedFunction.getStartBasicBlock());

                visitedBasicBlocks = new HashSet<>();
                dfsAndMerge(definedFunction.getStartBasicBlock());
            }
            // System.out.println (loopRemoved + " " + removed + " " + cleaned + " " + merged);
            if (!loopRemoved && !removed && !cleaned && !merged) break;
        }

        for (DefinedFunction definedFunction: ir.getAllDefinedFunctions()) {
            currentFunction = definedFunction;
            visitedBasicBlocks = new HashSet<>();
            dfsAndSolveRivalry (definedFunction.getStartBasicBlock());
        }

        new RegisterAllocator().solve(ir.getScope());

        for (DefinedFunction definedFunction: ir.getAllDefinedFunctions()) {
            currentFunction = definedFunction;
            definedFunction.resetUsedRegisters();
            visitedBasicBlocks = new HashSet<>();
            dfsAndCollectUsedRegisters (definedFunction.getStartBasicBlock());
        }

        return cfg;
    }

    private DefinedFunction currentFunction;
    private DefinedFunction currentInlineFunction;
    private Set<BasicBlock> visitedBasicBlocks;
    private Set<BasicBlock> visitedInlineBasicBlocks;
    private Map<Label, Label> labelReplacement;
    private Set<BasicBlock> stack;
    private boolean loopRemoved;
    private boolean removed;
    private boolean merged;
    private boolean cleaned;

    private Operand returnValue;

    private Map<cat.footoredo.mx.entity.Variable, cat.footoredo.mx.entity.Variable> replacement;

    private void dfsAndSetNotMemorable (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        for (Instruction instruction: currentBasicBlock.getInstructions()) {
            if (!instruction.isMemorable ()) {
                if (currentFunction.getName().equals("fuck")) {
                    System.out.println (instruction);
                }
                if (currentFunction.setNotMemorable()) {
                    notMemorableSet = true;
                }
                return;
            }
        }
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndSetNotMemorable(output);
    }

    private void dfsAndRemoveLoops (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        if (currentBasicBlock.isLoopHeader()) {
            // System.out.println (currentFunction.getName());
            if (currentBasicBlock.removeLoop()) {
                loopRemoved = true;
                return;
            }
        }
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndRemoveLoops(output);
    }

    private void dfsAndFindLoopVariants (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        for (BasicBlock loopHeader: currentBasicBlock.getBelongedLoopHeaders()) {
            /*currentBasicBlock.displayInstructions();
            System.out.println (currentBasicBlock.hasCall());
            System.out.println (loopHeader);*/
            loopHeader.setLoopHasCall(currentBasicBlock.hasCall());
            loopHeader.addLoopVariants(currentBasicBlock.getAffectedVariables());
        }
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndFindLoopVariants(output);
    }

    private Set<BasicBlock> loopVisitedBasicBlocks;
    private BasicBlock currentLoopHeader;

    private void loopDFS (BasicBlock currentBasicBlock) {
        loopVisitedBasicBlocks.add(currentBasicBlock);
        if (currentBasicBlock == currentLoopHeader.getLoopEnd())
            return;
        currentBasicBlock.addBelongedLoopHeader(currentLoopHeader);
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!loopVisitedBasicBlocks.contains(output))
                loopDFS(output);
    }

    private void dfsAndBuildLoop (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        if (currentBasicBlock.isLoopHeader()) {
            currentLoopHeader = currentBasicBlock;
            loopVisitedBasicBlocks = new HashSet<>();
            loopDFS (currentBasicBlock);
        }
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndBuildLoop(output);
    }

    private void dfsAndFindLoopHeader (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        if (currentBasicBlock.hasBackOutput()) {
            // System.out.println (currentBasicBlock.getOutputs().size());
            // currentBasicBlock.displayInstructions();
            BasicBlock loopHeader = currentBasicBlock.getBackOutput();
            loopHeader.setLoopEnd(cfg.get(loopHeader.getEndLabel()));
        }
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndFindLoopHeader(output);
    }

    private void dfsAndResetLoop (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        currentBasicBlock.resetLoop();
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndResetLoop(output);
    }

    private void dfsAndRemove (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        removed |= currentBasicBlock.remove();
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndRemove(output);
    }

    private void dfsAndResetLive (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        currentBasicBlock.resetLiveVariables();
        currentBasicBlock.resetLive();
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndResetLive(output);
    }

    private void dfsAndSolveRivalry (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        currentBasicBlock.solveRivalry();
        currentBasicBlock.solveUsedCount ();
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndSolveRivalry(output);
    }

    private void dfsAndCollectUsedRegisters (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        for (Instruction instruction: currentBasicBlock.getInstructions()) {
            if (((instruction instanceof CallInst) && ((CallInst) instruction).getFunction() instanceof BuiltinFunction) ||
                    (instruction instanceof MallocInst)) {
                // System.out.println("ere");
                currentFunction.setAllUsedRegisters();
            }
            else {
                for (Operand operand : instruction.getOperands()) {
                    if (operand.isRegister()) {
                        currentFunction.addUsedRegisters(operand.getRegister());
                    }
                }
            }
        }
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndCollectUsedRegisters(output);
    }

    private void dfsAndInlineCopy (BasicBlock currentBasicBlock, Label startLabel) {
        BasicBlock newBasicBlock = new BasicBlock(startLabel);
        visitedInlineBasicBlocks.add (currentBasicBlock);
        cfg.put(startLabel, newBasicBlock);
        // System.out.println ("inlined block: " + newBasicBlock);
        for (Instruction instruction: currentBasicBlock.getInstructions())
            if (instruction instanceof ReturnInst) {
                if (((ReturnInst) instruction).hasValue()) {
                    Operand value = ((ReturnInst) instruction).getValue().copy();
                    value.replace(replacement, currentFunction.getScope());
                    newBasicBlock.addInstruction(new AssignInst(returnValue, value, false));
                }
            }
            else {
                Instruction copiedInstruction = instruction.copy ();
                copiedInstruction.replace (replacement, currentFunction.getScope());
                newBasicBlock.addInstruction(copiedInstruction);
            }
        JumpInst jumpInst = currentBasicBlock.getJumpInst();
        if (jumpInst != null) {
            jumpInst = jumpInst.copy();
            jumpInst.replace(replacement, currentFunction.getScope());
            if (jumpInst instanceof UnconditionalJumpInst) {
                UnconditionalJumpInst unconditionalJumpInst = (UnconditionalJumpInst) jumpInst;
                Label originalLabel = unconditionalJumpInst.getTarget();
                // System.out.println ("target: " + originalLabel);
                if (labelReplacement.containsKey(originalLabel)) {
                    unconditionalJumpInst.setTarget(labelReplacement.get(originalLabel));

                    // System.out.println ("replaced to: " + unconditionalJumpInst.getTarget());
                }
                else {
                    Label newLabel = new Label ();
                    labelReplacement.put (originalLabel, newLabel);
                    unconditionalJumpInst.setTarget(newLabel);
                    BasicBlock nextBasicBlock = cfg.get(originalLabel);
                    if (visitedInlineBasicBlocks.contains(nextBasicBlock))
                        throw new Error ("WWWTTTTFFFF???");
                    dfsAndInlineCopy(nextBasicBlock, newLabel);
                }
            }
            else {
                ConditionalJumpInst conditionalJumpInst = (ConditionalJumpInst) jumpInst;
                Label originalLabel = conditionalJumpInst.getTrueTarget();
                if (labelReplacement.containsKey(originalLabel))
                    conditionalJumpInst.setTrueTarget(labelReplacement.get(originalLabel));
                else {
                    Label newLabel = new Label ();
                    labelReplacement.put (originalLabel, newLabel);
                    conditionalJumpInst.setTrueTarget(newLabel);
                    BasicBlock nextBasicBlock = cfg.get(originalLabel);
                    if (visitedInlineBasicBlocks.contains(nextBasicBlock))
                        throw new Error ("WWWTTTTFFFF???");
                    dfsAndInlineCopy(nextBasicBlock, newLabel);
                }
                originalLabel = conditionalJumpInst.getFalseTarget();
                if (labelReplacement.containsKey(originalLabel))
                    conditionalJumpInst.setFalseTarget(labelReplacement.get(originalLabel));
                else {
                    Label newLabel = new Label ();
                    labelReplacement.put (originalLabel, newLabel);
                    conditionalJumpInst.setFalseTarget(newLabel);
                    BasicBlock nextBasicBlock = cfg.get(originalLabel);
                    if (visitedInlineBasicBlocks.contains(nextBasicBlock))
                        throw new Error ("WWWTTTTFFFF???");
                    dfsAndInlineCopy(nextBasicBlock, newLabel);
                }
            }
        }
        newBasicBlock.setJumpInst(jumpInst);
    }

    private void dfsAndInline (BasicBlock currentBasicBlock, boolean inlineRecursion) {
        // System.out.println ("dfsAndInline: " + currentBasicBlock + " " + currentBasicBlock.isEndBlock() + " @ " + currentFunction.getName());
        visitedBasicBlocks.add(currentBasicBlock);
        List<Instruction> originalInstructions = new ArrayList<>(currentBasicBlock.getInstructions());
        List<Instruction> newInstructions = new ArrayList<>();
        for (Instruction instruction: originalInstructions) {
            if (instruction instanceof CallInst) {
                CallInst callInst = (CallInst) instruction;
                if (callInst.getFunction() instanceof DefinedFunction &&
                        (callInst.getFunction() != currentFunction || inlineRecursion)) {
                    inlined = true;
                    DefinedFunction inlineFunction = (DefinedFunction) (callInst.getFunction());
                    // System.out.println ("Inlining " + inlineFunction.getName() + " in " + currentFunction.getName());
                    replacement = new HashMap<>();
                    for (int i = 0; i < inlineFunction.getParameters().size(); ++ i) {
                        Parameter parameter = inlineFunction.getParameter(i);
                        cat.footoredo.mx.entity.Variable tmp = currentFunction.getScope().allocateTmpVariable(parameter.getType());
                        replacement.put (parameter, tmp);
                        newInstructions.add(new AssignInst(new VariableOperand(tmp), callInst.getArg(i), false));
                    }
                    returnValue = callInst.getResult();
                    currentInlineFunction = inlineFunction;
                    labelReplacement = new HashMap<>();
                    JumpInst originalJumpInst = currentBasicBlock.getJumpInst();
                    /*if (originalJumpInst == null) {
                        currentBasicBlock.displayInstructions();
                    }*/
                    boolean originalIsEndBlock = currentBasicBlock.isEndBlock();
                    BasicBlock startBasicBlock = inlineFunction.getStartBasicBlock();
                    Label inlineStartLabel = new Label();
                    Label inlineEndLabel = new Label ();
                    // System.out.println ("function end label: " + inlineFunction.getFunctionEndLabel());
                    labelReplacement.put (inlineFunction.getFunctionEndLabel(), inlineEndLabel);
                    currentBasicBlock.setJumpInst(new UnconditionalJumpInst(inlineStartLabel));
                    // System.out.println (newInstructions.size());
                    currentBasicBlock.setInstructions(newInstructions);
                    newInstructions = new ArrayList<>();
                    visitedInlineBasicBlocks = new HashSet<>();
                    dfsAndInlineCopy (startBasicBlock, inlineStartLabel);
                    currentBasicBlock = new BasicBlock(inlineEndLabel);
                    cfg.put (inlineEndLabel, currentBasicBlock);
                    currentBasicBlock.setJumpInst(originalJumpInst == null ? null : originalJumpInst.copy());
                    currentBasicBlock.setEndBlock(originalIsEndBlock);
                    // System.out.println ("asdasd\t" + currentBasicBlock + " " + originalIsEndBlock + " " + inlineEndLabel);
                }
                else {
                    newInstructions.add (instruction.copy());
                }
            }
            else {
                newInstructions.add (instruction.copy());
            }
        }

        // if (inlined) return;

        currentBasicBlock.setInstructions(newInstructions);

        for (Label outputLabel: currentBasicBlock.getOutputLabels()) {
            BasicBlock nextBasicBlock = cfg.get (outputLabel);
            // System.out.println ( "going to label " + outputLabel);
            if (!visitedBasicBlocks.contains(nextBasicBlock)) {
                dfsAndInline(nextBasicBlock, inlineRecursion);
            }
        }
    }

    private void dfsAndBuildCallGraph (BasicBlock currentBasicBlock) {
        // System.out.println ("SHOULDBE: " + currentBasicBlock + " " + currentBasicBlock.isEndBlock() + " @ " + currentFunction.getName());
        visitedBasicBlocks.add (currentBasicBlock);
        for (Instruction instruction: currentBasicBlock.getInstructions()) {
            if (instruction instanceof CallInst) {
                currentFunction.addCall((CallInst) instruction);
            }
        }
        for (Label output: currentBasicBlock.getOutputLabels()) {
            BasicBlock nextBasicBlock = cfg.get (output);
            if (!visitedBasicBlocks.contains(nextBasicBlock))
                dfsAndBuildCallGraph(nextBasicBlock);
        }
    }

    private void dfsAndCSE (BasicBlock currentBasicBlock) {
        // System.out.println ("SHOULDBE: " + currentBasicBlock + " " + currentBasicBlock.isEndBlock() + " @ " + currentFunction.getName());
        visitedBasicBlocks.add (currentBasicBlock);
        currentBasicBlock.CSE();
        for (Label output: currentBasicBlock.getOutputLabels()) {
            BasicBlock nextBasicBlock = cfg.get (output);
            if (!visitedBasicBlocks.contains(nextBasicBlock))
                dfsAndCSE(nextBasicBlock);
        }
    }

    private void dfsAndLink (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add (currentBasicBlock);
        stack.add (currentBasicBlock);
        // System.out.println (currentBasicBlock + " " + currentBasicBlock.isEndBlock() + " @ " + currentFunction.getName());
        if (currentBasicBlock.isEndBlock()) {
            // System.out.println (currentFunction.getName());
            currentFunction.setEndBasicBlock(currentBasicBlock);
        }
        for (Label outputLabel: currentBasicBlock.getOutputLabels()) {
            BasicBlock nextBasicBlock = cfg.get (outputLabel);
            nextBasicBlock.addInput(currentBasicBlock);
            if (!visitedBasicBlocks.contains(nextBasicBlock)) {
                currentBasicBlock.addOutput(nextBasicBlock);
                dfsAndLink(nextBasicBlock);
            }
            else if (stack.contains (nextBasicBlock)){
                currentBasicBlock.setBackOutput(nextBasicBlock);
            }
            else {
                currentBasicBlock.addOutput(nextBasicBlock);
            }
        }
        stack.remove (currentBasicBlock);
    }

    private void dfsAndResetInputOutput (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add (currentBasicBlock);
        currentBasicBlock.resetInputs();
        currentBasicBlock.resetOutputs();
        for (Label label: currentBasicBlock.getOutputLabels()) {
            BasicBlock output = cfg.get (label);
            if (!visitedBasicBlocks.contains(output))
                dfsAndResetInputOutput(output);
        }
    }

    private void dfsAndClean (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        cleaned |= currentBasicBlock.cleanInstructions();
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndClean(output);
    }

    private void dfsAndMerge (BasicBlock currentBasicBlock) {
        visitedBasicBlocks.add(currentBasicBlock);
        if (currentBasicBlock.getAllOutputs().size() == 0) return;
        if (currentBasicBlock.getOutputs().size() == 1 && !currentBasicBlock.hasBackOutput()) {
            BasicBlock output = currentBasicBlock.getOutputs().iterator().next();
            if (output.getInputs().size() == 1) {
                merged = true;
                // System.out.println ("ss");
                // System.out.println (output + " "  + currentBasicBlock);
                currentBasicBlock.merge(output);
                // return;
            }
        }
        for (BasicBlock output: currentBasicBlock.getOutputs())
            if (!visitedBasicBlocks.contains(output))
                dfsAndMerge(output);
    }

    private void backPropagate (BasicBlock currentBasicBlock, Set <cat.footoredo.mx.entity.Variable> liveVariables) {
        boolean isUpdated = false;
        if (!currentBasicBlock.backPropagated()) {
            isUpdated = true;
            currentBasicBlock.prepareForFirstBackPropagate();
        }
        //System.out.println ("backPropagate " + currentBasicBlock);
        for (cat.footoredo.mx.entity.Variable variable: liveVariables) {
            // System.out.println (variable.getName());
            if (!currentBasicBlock.isLiveVariable(variable)) {
                // System.err.println ("ere");
                isUpdated = true;
                if (variable == null) {
                    throw new Error("ss");
                }
                currentBasicBlock.addLiveVariable(variable);
            }
        }
        if (isUpdated) {
            /*System.out.println ("in function " + currentFunction.getName());
            for (cat.footoredo.mx.entity.Variable variable: liveVariables)
                System.out.println ("current live: " + variable.getName());*/
            // currentBasicBlock.displayInstructions();
            Set <cat.footoredo.mx.entity.Variable> newLiveVariables = currentBasicBlock.backPropagate ();
            for (BasicBlock input: currentBasicBlock.getInputs()) {
                backPropagate(input, new HashSet<>(newLiveVariables));
            }
        }
    }

    private BasicBlock currentBasicBlock;
    private void processDefinedFunction (DefinedFunction definedFunction) {
        Symbol symbol = new NamedSymbol("@@CFG_function_start__" + definedFunction.getSymbolString());
        Label label = new Label(symbol);
        definedFunction.setCFGSymbol (symbol);
        newBasicBlock(label);
        definedFunction.setStartBasicBlock(currentBasicBlock);
        for (Statement statement: definedFunction.getIR()) {
            processStatement(statement);
        }
        if (currentBasicBlock == null)
            throw new Error("FFFAFAF");
        insert (new ULTIMATERETURNINST());
        currentBasicBlock = null;
    }

    private VariableOperand tmpVariable (Type type) {
        cat.footoredo.mx.type.Type typeType = type == Type.INT64 ? new PointerType() : new BooleanType();
        return new VariableOperand(currentFunction.getScope().allocateTmpVariable(typeType));
    }

    private void newBasicBlock (Label label) {
        currentBasicBlock = new BasicBlock(label);
        cfg.put (label, currentBasicBlock);
    }

    private Operand processExpression (Expression expression) {
        return expression.accept(this);
    }

    private void processStatement (Statement statement) {
        statement.accept(this);
    }

    private void insert (Instruction instruction) {
        if (currentBasicBlock != null)
            currentBasicBlock.addInstruction(instruction);
    }

    private void jump (JumpInst jumpInst) {
        if (currentBasicBlock != null)
            currentBasicBlock.setJumpInst(jumpInst);
        currentBasicBlock = null;
    }

    private Operand operand (Variable variable) {
        return new VariableOperand((cat.footoredo.mx.entity.Variable) variable.getEntity());
    }

    private Operand operand (Integer integerLiteral) {
        return new ConstantIntegerOperand(integerLiteral.getType(), integerLiteral.getValue());
    }

    private Operand operand (String stringLiteral) {
        return new ConstantStringOperand(stringLiteral.getEntry());
    }

    @Override
    public Void visit(ExpressionStatement s) {
        processExpression(s.getExpression());
        return null;
    }

    @Override
    public Void visit(Assign s) {
        Operand right = processExpression(s.getRhs());
        insert (new AssignInst(processExpression(s.getLhs()), right, s.getLhs() instanceof Memory));
        return null;
    }

    private void cjump (Operand condition, Label trueLabel, Label falseLabel) {
        if (condition instanceof ConstantIntegerOperand) {
            boolean value = ((ConstantIntegerOperand) condition).getValue() != 0;
            if (value) {
                jump (new UnconditionalJumpInst(trueLabel));
            }
            else {
                jump (new UnconditionalJumpInst(falseLabel));
            }
        }
        else {
            jump (new ConditionalJumpInst(condition, trueLabel, falseLabel));
        }
    }

    @Override
    public Void visit(CJump s) {
        cjump (processExpression(s.getCond()), s.getThenLabel(), s.getElseLabel());
        return null;
    }

    @Override
    public Void visit(Jump s) {
        jump (new UnconditionalJumpInst(s.getTarget()));
        return null;
    }

    @Override
    public Void visit(LabelStatement s) {
        if (currentBasicBlock != null) {
            jump (new UnconditionalJumpInst(s.getLabel()));
        }
        newBasicBlock(s.getLabel());
        return null;
    }

    @Override
    public Void visit(Return s) {
        if (s.hasExpression()) {
            // System.out.println ("asd");
            insert (new ReturnInst(processExpression(s.getExpression())));
        }
        else {
            insert (new ReturnInst());
        }
        jump (new UnconditionalJumpInst(s.getFunctionEndLabel()));
        return null;
    }

    @Override
    public Operand visit(Null s) {
        return new ConstantIntegerOperand(Type.INT64, 0);
    }

    @Override
    public Operand visit(Unary s) {
        VariableOperand tmp = tmpVariable (s.getType());
        insert (new UnaryInst(tmp, s.getOp(), processExpression(s.getExpression())));
        return tmp;
    }

    private Operand integer (Type t, long value) {
        return new ConstantIntegerOperand(t, value);
    }

    private Operand integer (Type t, boolean value) {
        return new ConstantIntegerOperand(t, value ? 1 : 0);
    }


    private Operand preprocessBinary(Type t, long left, Op op, long right) {
        switch (op) {
            case ADD: return integer(t, left + right);
            case SUB: return integer(t, left - right);
            case MUL: return integer(t, left * right);
            case S_DIV: return integer(t, left / right);
            case S_MOD: return integer(t, left % right);
            case U_DIV: return integer(t, left / right);
            case U_MOD: return integer(t, left % right);
            case BIT_AND: return integer(t, left & right);
            case BIT_OR: return integer(t, left | right);
            case BIT_XOR: return integer(t, left ^ right);
            case BIT_LSHIFT: return integer(t, left << right);
            case BIT_RSHIFT: return integer(t, left >> right);
            case ARITH_RSHIFT: return integer(t, left << right);
            default:
                switch (op) {
                    case EQ: return integer(t, left == right);
                    case NEQ: return integer(t, left != right);
                    case S_GT: return integer(t, left > right);
                    case S_GTEQ: return integer(t, left >= right);
                    case S_LT: return integer(t, left < right);
                    case S_LTEQ: return integer(t, left <= right);
                    case U_GT: return integer(t, left > right);
                    case U_GTEQ: return integer(t, left >= right);
                    case U_LT: return integer(t, left < right);
                    case U_LTEQ: return integer(t, left <= right);
                    default:
                        throw new Error ("unknown binary operator: " + op);
                }
        }
    }

    @Override
    public Operand visit(Binary s) {
        Operand right = processExpression(s.getRhs());
        Operand left = processExpression(s.getLhs());
        if (left instanceof ConstantIntegerOperand && right instanceof ConstantIntegerOperand) {
            return preprocessBinary (s.getType(), ((ConstantIntegerOperand) left).getValue(), s.getOp(), ((ConstantIntegerOperand) right).getValue());
        }
        else {
            VariableOperand tmp = tmpVariable(s.getType());
            insert(new BinaryInst(tmp, left, s.getOp(), right));
            return tmp;
        }
    }

    @Override
    public Operand visit(Call s) {
        List<Operand> parameters = new ArrayList<>();
        for (int i = 0; i < s.getArgc(); ++ i) {
            parameters.add (processExpression(s.getArg(i)));
        }
        if (s.getFunction().getReturnType() instanceof VoidType) {
            insert (new CallInst(null, s.getFunction(), parameters));
            return new ConstantIntegerOperand(Type.INT8, 0);
        }
        else {
            VariableOperand tmp = tmpVariable(s.getType());
            insert(new CallInst(tmp, s.getFunction(), parameters));
            return tmp;
        }
    }

    @Override
    public Operand visit(Memory s) {
        VariableOperand tmp = tmpVariable (s.getType());
        insert (new DereferenceInst(tmp, processExpression(s.getExpression())));
        return tmp;
    }

    @Override
    public Operand visit(Variable s) {
        return new VariableOperand((cat.footoredo.mx.entity.Variable) s.getEntity());
    }

    @Override
    public Operand visit(Integer s) {
        return new ConstantIntegerOperand(s.getType(), s.getValue());
    }

    @Override
    public Operand visit(String s) {
        return new ConstantStringOperand(s.getEntry());
    }

    @Override
    public Operand visit(Malloc s) {
        VariableOperand tmp = tmpVariable(Type.INT64);
        insert (new MallocInst(tmp, processExpression(s.getSize())));
        return tmp;
    }
}
