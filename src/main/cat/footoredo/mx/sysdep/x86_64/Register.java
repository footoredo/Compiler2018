package cat.footoredo.mx.sysdep.x86_64;

import cat.footoredo.mx.asm.SymbolTable;
import cat.footoredo.mx.asm.Type;
import jdk.nashorn.internal.ir.annotations.Ignore;

public class Register extends cat.footoredo.mx.asm.Register {
    private long number;
    private String baseName;
    private Type type;

    public Register(long number, Type type) {
        if (number < 8) {
            this.baseName = RegisterClass.values()[(int)number].name().toLowerCase();
        }
        if (type == null) {
            throw new Error ("wtf??");
        }
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

    private String getBaseName () {
        return baseName;
    }

    @Override
    public String toSource (SymbolTable table) {
        return typedName ();
    }

    private String typedName () {
        if (number < 8) {
            switch (type) {
                case INT8:
                    return lowerByteRegister();
                case INT16:
                    return getBaseName();
                case INT32:
                    return "e" + getBaseName();
                case INT64:
                    return "r" + getBaseName();
                default:
                    throw new Error("unknown register Type: " + type);
            }
        }
        else {
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
    }


    private String lowerByteRegister () {
        switch ((int)number) {
            case 0:
            case 1:
            case 2:
            case 3: return getBaseName().substring(0, 1) + "l";
            default:
                throw new Error("does not have lower-byte register: " + getBaseName());
        }
    }
}
