package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class ArrayTypeRef extends TypeRef {
    public static final int undefined = -1;
    protected TypeRef baseType;
    protected int len;

    public ArrayTypeRef (Location location, TypeRef baseType, int len) {
        super (location);
        this.baseType = baseType;
        this.len = len;
    }

    public ArrayTypeRef (Location location, TypeRef baseType) {
        super (location);
        this.baseType = baseType;
        this.len = undefined;
    }

    public TypeRef getBaseType () {
        return baseType;
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
