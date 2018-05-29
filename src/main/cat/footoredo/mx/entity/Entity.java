package cat.footoredo.mx.entity;

import cat.footoredo.mx.asm.ImmediateValue;
import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Operand;
import cat.footoredo.mx.asm.Register;
import cat.footoredo.mx.ast.TypeNode;
import cat.footoredo.mx.type.Type;

abstract public class Entity {
    protected TypeNode typeNode;
    protected String name;
    private int referredCount;
    private MemoryReference memoryReference;
    private Operand address;
    private Register register;

    public Entity (TypeNode typeNode, String name) {
        this.typeNode = typeNode;
        this.name = name;
        this.referredCount = 0;
    }

    public String getName() {
        return name;
    }

    abstract public String getSymbolString ();

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public Type getType() {return typeNode.getType();}

    public Location getLocation () {
        return typeNode.getLocation();
    }

    public void referred () {
        referredCount ++;
    }

    public boolean isReferred() { return referredCount > 0; }

    public int allocateSize () {
        return getType().allocateSize();
    }

    public int size () {
        return getType().size();
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public MemoryReference getMemoryReference() {
        // checkAddress ();
        return memoryReference;
    }

    public void setMemoryReference(MemoryReference memoryReference) {
        this.memoryReference = memoryReference;
    }

    public Operand getAddress() {
        // checkAddress ();
        return address;
    }

    public void setAddress(MemoryReference address) {
        this.address = address;
    }

    public void setAddress(ImmediateValue address) {
        this.address = address;
    }

    private void checkAddress () {
        if (memoryReference == null && address == null && register == null) {
            throw new Error("address did not resolved: " + name);
        }
    }

    abstract public <T> T accept(EntityVisitor<T> visitor);
}
