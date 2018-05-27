package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.ImmediateValue;
import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Operand;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.Entity;

abstract public class Expression {
    private final Type type;

    public Expression(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    abstract public <S, E> E accept (IRVisitor<S, E> visitor);

    public boolean isAddress () { return false; }
    public boolean isVariable () { return false; }
    public boolean isConstant () { return false; }

    public Operand getAddress () {
        throw new Error("Expression#getAddress called");
    }

    public MemoryReference getMemoryReference () {
        throw new Error("Expression#getMemoryReference called");
    }

    public Expression getAddressNode (Type type) {
        throw new Error("unexpected node for LHS: " + getClass());
    }

    public ImmediateValue getAsmValue () {
        throw new Error ("#Expression#getAsmValue called");
    }

    public Entity getEntityForce () {
        return null;
    }
}
