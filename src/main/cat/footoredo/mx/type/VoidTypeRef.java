package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class VoidTypeRef extends TypeRef {
    public VoidTypeRef (Location location) {
        super (location);
    }
    public String toString () {
        return "void";
    }
}
