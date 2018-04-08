package cat.footoredo.mx.type;

public class ArrayTypeRef extends TypeRef {
    protected TypeRef baseType;

    public ArrayTypeRef (Typeref baseType) {
        super ();
        this.baseType = baseType;
    }

    public TypeRef getBaseType () {
        return baseType;
    }
}
