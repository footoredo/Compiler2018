package cat.footoredo.mx.entity;

import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Operand;
import cat.footoredo.mx.asm.Register;
import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.ir.Expression;
import cat.footoredo.mx.ir.Memory;
import cat.footoredo.mx.type.Type;

public class Variable extends Entity {
    private ExpressionNode initializer;
    private Expression ir;

    private MemoryReference memory;
    private Register register;

    private Boolean isGlobal;

    public Variable (VariableDeclarationNode variableDeclarationNode) {
        super (variableDeclarationNode.getTypeNode(), variableDeclarationNode.getName());
        this.initializer = variableDeclarationNode.getInitExpr();
        this.isGlobal = false;
    }

    public Variable (TypeNode type, String name) {
        super (type, name);
        this.initializer = null;
        this.isGlobal = false;
    }

    public Boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(Boolean global) {
        isGlobal = global;
    }

    private static int tmpCounter = 0;

    static public Variable tmp (Type type) {
        return new Variable(new TypeNode(type), "@tmp" + tmpCounter ++);
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

    public void setRegister(Register register) {
        this.register = register;
    }

    public Operand getSpace () {
        if (isRegister()) return register;
        else return memory;
    }
}
