package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.ImmediateValue;
import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Register;
import cat.footoredo.mx.asm.Type;

abstract public class Operand {
    public boolean isRegister () {
        return false;
    }
    public boolean isConstant () {
        return false;
    }
    public boolean isMemory () {
        return false;
    }
    public MemoryReference getMemoryReference () {
        throw new Error ("no memory refernce found.");
    }
    public ImmediateValue getImmediateValue () {
        throw new Error ("no immediate value found.");
    }
    public Register getRegister () {
        throw new Error ("no register value found.");
    }
    public cat.footoredo.mx.asm.Operand toASMOperand() {
        if (isConstant()) return getImmediateValue();
        else if (isRegister()) return getRegister();
        else return getMemoryReference();
    }

    private Type type;
    Operand (Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
