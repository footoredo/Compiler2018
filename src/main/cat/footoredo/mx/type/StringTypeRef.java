package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class StringTypeRef extends TypeRef {
    public StringTypeRef(Location location) {
        super (location);
    }
    public StringTypeRef() {
        super (null);
    }
    public String toString () {
        return "string";
    }
}
