package cat.footoredo.mx.sysdep.x86_64;

import cat.footoredo.mx.asm.SymbolTable;
import cat.footoredo.mx.asm.Type;

public class Register extends cat.footoredo.mx.asm.Register {
    private long number;
    private Type type;

    public Register(long number, Type type) {
        this.number = number;
        this.type = type;
    }

    public Register(long number) {
        this (number, Type.INT64);
    }

    public Register(RegisterClass registerClass, Type type) {
        this (registerClass.getValue(), type);
    }

    public Register(RegisterClass registerClass) {
        this (registerClass, Type.INT64);
    }

    public Register forType (Type type) {
        return new Register(number, type);
    }

    public boolean equals (Register register) {
        return number == register.number;
    }

    public int hashCode () {
        return Long.hashCode(number);
    }

    public long getNumber() {
        return number;
    }

    public Type getType() {
        return type;
    }

    public String getBaseName () {
        return Long.toString(number).toLowerCase();
    }

    @Override
    public String toSource (SymbolTable table) {
        return typedName ();
    }

    /*private String typedName () {
        switch (type) {
            case INT8: return lowetByteRegister ();
            case INT16: return getBaseName();
            case INT32: return "e" + getBaseName();
            case INT64: return "r" + getBaseName();
            default:
                throw new Error("unknown register Type: " + type);
        }
    }*/

    private String typedName () {
        String name = "r" + number;
        switch (type) {
            case INT8: return name + "b";
            case INT16: return name + "w";
            case INT32: return name + "d";
            case INT64: return name;
            default:
                throw new Error("unknown type " + type);
        }
    }

    /*
    private String lowetByteRegister () {
        switch (registerClass) {
            case AX:
            case BX:
            case CX:
            case DX: return getBaseName().substring(0, 1) + "l";
            default:
                throw new Error("does not have lower-byte register: " + _class);
        }
    }*/
}
