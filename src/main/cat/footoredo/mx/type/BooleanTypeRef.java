package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class BooleanTypeRef extends TypeRef {
    public BooleanTypeRef(Location location) {
        super (location);
    }
    public BooleanTypeRef() {
        super (null);
    }
    public String toString() {
        return "bool";
    }
}
