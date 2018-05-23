package cat.footoredo.mx.type;

abstract public class Type {
    static final public int sizeUnknown = -1;

    abstract public int size ();
    public int allocateSize () {
        return size ();
    }

    public boolean isVoid() {
        return this instanceof VoidType;
    }
    public boolean isArray() {
        return this instanceof ArrayType;
    }
    public boolean isFunction() {
        return this instanceof FunctionType;
    }
    public boolean isClass() {
        return this instanceof ClassType;
    }
    public boolean isString() {
        return this instanceof StringType;
    }
    public boolean isInteger() {
        return this instanceof IntegerType;
    }
    public boolean isBoolean() {
        return this instanceof BooleanType;
    }
    public boolean isNull() {
        return this instanceof NullType;
    }

    public boolean isSigned() { throw new Error("#isSigned for non-integer type"); }

    public int hashCode() {
        return toString().hashCode();
    }
}
