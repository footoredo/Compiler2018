package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.*;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StringType;

public class TypeNode extends Node {
    TypeRef typeRef;
    Type type;

    public TypeNode (TypeRef typeRef) {
        super ();
        this.typeRef = typeRef;
        this.type = null;
    }

    public TypeNode(Type type) {
        super();
        this.type = type;
        this.typeRef = null;
    }

    public TypeRef getTypeRef() {
        return typeRef;
    }

    public Type getType() { return type; }

    public Location getLocation () {
        return typeRef.getLocation ();
    }

    public String toString () {
        if (typeRef != null)
            return typeRef.toString();
        else
            return type.toString();
    }

    public boolean isVoid() {
        if (typeRef != null) return typeRef instanceof VoidTypeRef;
        else return type instanceof VoidType;
    }

    public boolean isArray() {
        if (typeRef != null) return typeRef instanceof ArrayTypeRef;
        else return type instanceof ArrayType;
    }

    public boolean isFunction() {
        if (typeRef != null) return typeRef instanceof FunctionTypeRef;
        else return type instanceof FunctionType;
    }

    public boolean isClass() {
        if (typeRef != null) return typeRef instanceof ClassTypeRef;
        else return type instanceof ClassType;
    }

    public boolean isString() {
        if (typeRef != null) return typeRef instanceof StringTypeRef;
        else return type instanceof StringType;
    }

    public boolean isInteger() {
        if (typeRef != null) return typeRef instanceof IntegerTypeRef;
        else return type instanceof IntegerType;
    }

    public boolean isResolved() {
        return type != null;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
