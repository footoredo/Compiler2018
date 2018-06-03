package cat.footoredo.mx.entity;

import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Operand;
import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.ir.Expression;
import cat.footoredo.mx.ir.Memory;
import cat.footoredo.mx.sysdep.x86_64.Register;
import cat.footoredo.mx.type.Type;

import java.util.HashSet;
import java.util.Set;

public class Variable extends Entity {
    private ExpressionNode initializer;
    private Expression ir;

    private MemoryReference memory;
    private Register register;

    private Boolean isGlobal;

    private Set<Variable> rivalries;
    private Set<Variable> originalRivalries;

    private int usedCount;

    public Variable (VariableDeclarationNode variableDeclarationNode) {
        super (variableDeclarationNode.getTypeNode(), variableDeclarationNode.getName());
        this.initializer = variableDeclarationNode.getInitExpr();
        this.isGlobal = false;
        this.rivalries = new HashSet<>();
        this.originalRivalries = new HashSet<>();
        this.usedCount = 0;
    }

    public Variable (TypeNode type, String name) {
        super (type, name);
        this.initializer = null;
        this.isGlobal = false;
        this.rivalries = new HashSet<>();
        this.originalRivalries = new HashSet<>();
        this.usedCount = 0;
    }

    public Boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(Boolean global) {
        isGlobal = global;
    }

    public void addRivalry (Variable rivalry) {
        if (isGlobal()) return;
        if (rivalry != this && !rivalry.isGlobal()) {
            // System.out.println (name + " <-> " + rivalry.getName());
            rivalries.add(rivalry);
            originalRivalries.add(rivalry);
        }
    }

    public void removeRivalry (Variable rivalry) {
        rivalries.remove(rivalry);
    }

    public Set<Variable> getRivalries() {
        return rivalries;
    }

    public Set<Variable> getOriginalRivalries() {
        return originalRivalries;
    }

    public void disconnect () {
        for (Variable rivalry: rivalries)
            rivalry.removeRivalry(this);
        // rivalries = null;
    }

    public void addUsedCount () {
        usedCount ++;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public boolean isUsed () {
        return usedCount > 0;
    }

    public int getRivalryCount () {
        return rivalries.size();
    }

    private static int tmpCounter = 0;

    static public Variable tmp (Type type) {
        return new Variable(new TypeNode(type), "@tmp" + tmpCounter ++);
    }

    private boolean isTmp () {
        return name.length() > 4 && name.substring(0, 4).equals("@tmp");
    }

    public double getScore () {
        return (double) getRivalryCount() / getUsedCount() + (isTmp() ? -50 : 0);
    }

    public void setIR(Expression ir) {
        this.ir = ir;
    }

    public Expression getIR () {
        return ir;
    }

    public ExpressionNode getInitializer() {
        return initializer;
    }

    public boolean hasInitializer() { return initializer != null; }

    public boolean isStatic () {
        return hasInitializer() && initializer instanceof IntegerLiteralNode;
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getSymbolString() {
        return name;
    }

    public boolean isRegister () {
        return register != null;
    }

    public boolean isMemory () {
        return memory != null;
    }

    public Register getRegister () {
        return register;
    }

    public MemoryReference getMemory() {
        return memory;
    }

    public void setMemory(MemoryReference memory) {
        this.memory = memory;
    }

    public void setRegister(long index) {
        // System.out.println ("set " + name + " to r" + index);
        this.register = new Register(index, cat.footoredo.mx.asm.Type.get(size()));
    }

    public Operand getSpace () {
        if (isRegister()) return register;
        else return memory;
    }
}
