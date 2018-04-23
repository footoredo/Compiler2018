package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class StringTypeRef extends BuiltinTypeRef {
    public StringTypeRef(Location location) {
        super (location);
    }
    public StringTypeRef() {
        super (null);
    }
    public String toString () {
        return "string";
    }

    @Override
    public Type definingType() {
        return new StringType();
    }

    public boolean equals(Object other) {
        return (other instanceof StringTypeRef);
    }
}
