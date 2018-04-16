package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class ArrayTypeRef extends TypeRef {
    public static final int undefined = -1;
    protected TypeRef baseType;
    protected int len;
    protected int dim;

    public ArrayTypeRef (TypeRef baseType, int len) {
        super (baseType.getLocation());
        this.baseType = baseType;
        this.len = len;
        if (baseType instanceof ArrayTypeRef) {
            dim = ((ArrayTypeRef) baseType).getDim() + 1;
        }
        else {
            dim = 1;
        }
    }

    public ArrayTypeRef (TypeRef baseType) {
        super (baseType.getLocation());
        this.baseType = baseType;
        this.len = undefined;
        if (baseType instanceof ArrayTypeRef) {
            dim = ((ArrayTypeRef) baseType).getDim() + 1;
        }
        else {
            dim = 1;
        }
    }

    public TypeRef getBaseType () {
        return baseType;
    }

    public int getDim() {
        return dim;
    }

    public int getLen() {
        return len;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append (baseType.toString ());
        buf.append ("[");
        if (len != undefined)
            buf.append (Integer.toString (len));
        buf.append ("]");
        return buf.toString();
    }
}
