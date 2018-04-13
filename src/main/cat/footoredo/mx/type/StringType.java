package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class StringType extends TypeRef {
    public StringType (Location location) {
        super (location);
    }
    public String toString () {
        return "string";
    }
}
