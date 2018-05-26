package cat.footoredo.mx.sysdep.x86_64;

import cat.footoredo.mx.asm.SymbolTable;
import cat.footoredo.mx.asm.Type;

public class Register extends cat.footoredo.mx.asm.Register {
    private RegisterClass registerClass;
    private Type type;

    public Register(RegisterClass registerClass, Type type) {
        this.registerClass = registerClass;
        this.type = type;
    }

    public boolean equals (Register register) {
        return registerClass.equals(register.registerClass);
    }

    public int hashCode () {
        return registerClass.hashCode();
    }

    public RegisterClass getRegisterClass() {
        return registerClass;
    }

    public Type getType() {
        return type;
    }

    public String getBaseName () {
        return registerClass.toString().toLowerCase();
    }

    @Override
    public String toSource (SymbolTable table) {
        return typedName ();
    }

    private String typedName () {
        switch (type) {
            case INT8: return lowetByteRegister ();
            case INT16: return getBaseName();
            case INT32: return "e" + getBaseName();
            case INT64: return "r" + getBaseName();
            default:
                throw new Error("unknown register Type: " + type);
        }
    }

    private String lowetByteRegister () {
        switch (registerClass) {
            case AX:
            case BX:
            case CX:
            case DX: return getBaseName().substring(0, 1) + "l";
            default:
                throw new Error("does not have lower-byte register: " + _class);
        }
    }
}
