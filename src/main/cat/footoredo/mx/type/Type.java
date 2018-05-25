package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.MemberNode;

abstract public class Type {
    static final public int sizeUnknown = -1;

    abstract public int size ();
    public int allocateSize () {
        return 0;
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

    public boolean isScaler () {
        return true;
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public MemberType getMemberType () {
        throw new Error ("requesting member type from non-member type");
    }
}
