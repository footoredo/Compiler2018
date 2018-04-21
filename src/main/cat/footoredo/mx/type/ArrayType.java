package cat.footoredo.mx.type;

public class ArrayType extends Type {
    private Type baseType;
    private long length;
    static final private long undefined = -1;

    public ArrayType(Type baseType, long length) {
        this.baseType = baseType;
        this.length = length;
    }

    public ArrayType(Type baseType) {
        this(baseType, undefined);
    }

    public Type getBaseType() {
        return baseType;
    }

    public ArrayType(long length) {
        this.length = length;
    }

    public String toString() {
        if (length == undefined) {
            return baseType.toString() + "[]";
        }
        else {
            return baseType.toString() + "[" + length + "]";
        }
    }
}
