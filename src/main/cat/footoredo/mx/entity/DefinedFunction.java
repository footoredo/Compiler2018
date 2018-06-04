package cat.footoredo.mx.entity;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.asm.Symbol;
import cat.footoredo.mx.ast.BlockNode;
import cat.footoredo.mx.ast.MethodNode;
import cat.footoredo.mx.cfg.BasicBlock;
import cat.footoredo.mx.cfg.CallInst;
import cat.footoredo.mx.cfg.RegisterAllocator;
import cat.footoredo.mx.ir.Statement;
import cat.footoredo.mx.sysdep.x86_64.CodeGenerator;
import cat.footoredo.mx.sysdep.x86_64.Register;
import cat.footoredo.mx.sysdep.x86_64.RegisterClass;

import java.util.*;

public class DefinedFunction extends Function {
    BlockNode block;
    LocalScope scope;
    List<Statement> IR;
    Symbol CFGSymbol;

    private BasicBlock startBasicBlock;
    private BasicBlock endBasicBlock;

    private boolean isStatic;

    private Set<DefinedFunction> calls, called;

    public static final Set<Register> allRegisters = new HashSet<>();
    public static final Set<Register> calleeSaveRegisters = new HashSet<>();

    private Set <Register> usedRegisters;

    private Label functionEndLabel;

    private Variable solved;
    private Variable answer;

    private boolean isMemorable;

    public boolean isMemorable () {
        return isMemorable;
    }

    public boolean isFibLike () {
        if (!isMemorable) return false;
        if (getParameters().size() != 1) return false;
        if (!getParameter(0).getType().isInteger()) return false;
        return true;
    }

    public boolean setNotMemorable () {
        if (isMemorable) {
            isMemorable = false;
            for (DefinedFunction caller: called) {
                caller.setNotMemorable();
            }
            return true;
        }
        else {
            return false;
        }
    }

    public String getSolvedName () {
        return "__TAT_solved__" + getName();
    }

    public String getAnswerName () {
        return "__TAT_answer__" + getName();
    }

    public Variable getSolved() {
        return solved;
    }

    public void setSolved(Variable solved) {
        this.solved = solved;
    }

    public Variable getAnswer() {
        return answer;
    }

    public void setAnswer(Variable answer) {
        this.answer = answer;
    }

    static {
        for (long index: RegisterAllocator.AVAILABLE_REGISTERS) {
            allRegisters.add(new Register(index));
        }
        for (long index: CodeGenerator.CALLEE_SAVE_REGISTERS) {
            calleeSaveRegisters.add(new Register(index));
        }
    }

    public DefinedFunction (MethodNode methodNode, String parentClass) {
        super (methodNode.getTypeNode(), methodNode.getMethodDescription(), parentClass);
        // System.out.println (parentClass + "#" + methodNode.getName());
        this.block = methodNode.getBlock();
        this.calls = new HashSet<>();
        this.called = new HashSet<>();
        this.functionEndLabel = null;
        this.isMemorable = true;
    }

    public void resetUsedRegisters () {
        usedRegisters = new HashSet<>();
    }

    public void addUsedRegisters (Register register) {
        if (allRegisters.contains(register) && !calleeSaveRegisters.contains(register))
            usedRegisters.add(register);
    }

    public Set<Register> getUsedRegisters () {
        return usedRegisters;
    }

    public void setAllUsedRegisters () {
        for (Register register: allRegisters) {
            if (!calleeSaveRegisters.contains(register)) {
                usedRegisters.add(register);
            }
        }
    }

    public void addCall (CallInst callInst) {
        if (callInst.getFunction() instanceof DefinedFunction) {
            calls.add((DefinedFunction) callInst.getFunction());
            ((DefinedFunction) callInst.getFunction()).addCalled(this);
        }
    }

    private void addCalled (DefinedFunction caller) {
        called.add (caller);
    }

    public void resetCalls () {
        calls = new HashSet<>();
    }

    public boolean callItself () {
        for (DefinedFunction function: calls)
            if (function == this)
                return true;
        return false;
    }

    public Label getFunctionEndLabel() {
        return functionEndLabel;
    }

    public void setFunctionEndLabel(Label functionEndLabel) {
        this.functionEndLabel = functionEndLabel;
    }

    public Set<DefinedFunction> getCalls() {
        return calls;
    }

    public int getCallCount () {
        return calls.size();
    }

    public boolean hasCall () {
        return !calls.isEmpty();
    }

    public void appendFront (List<Statement> statements) {
        statements.addAll(IR);
        IR = statements;
    }

    public BasicBlock getStartBasicBlock() {
        return startBasicBlock;
    }

    public void setStartBasicBlock(BasicBlock startBasicBlock) {
        this.startBasicBlock = startBasicBlock;
    }

    public BasicBlock getEndBasicBlock() {
        return endBasicBlock;
    }

    public void setEndBasicBlock(BasicBlock endBasicBlock) {
        this.endBasicBlock = endBasicBlock;
    }

    public Symbol getCFGSymbol() {
        return CFGSymbol;
    }

    public void setCFGSymbol(Symbol CFGSymbol) {
        this.CFGSymbol = CFGSymbol;
    }

    public DefinedFunction (MethodNode methodNode) {
        this (methodNode, null);
    }

    public BlockNode getBlock() {
        return block;
    }

    public LocalScope getScope() {
        return scope;
    }

    public LocalScope getLvarScope () {
        return block.getScope();
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    public List<Statement> getIR() {
        return IR;
    }

    public void setIR(List<Statement> IR) {
        this.IR = IR;
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
