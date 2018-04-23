package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class BooleanTypeRef extends BuiltinTypeRef implements SelfDefiningType {
    public BooleanTypeRef(Location location) {
        super (location);
    }
    public BooleanTypeRef() {
        super (null);
    }
    public String toString() {
        return "bool";
    }

    public Type definingType() {
        return new BooleanType();
    }

    public boolean equals(Object other) {
        return (other instanceof BooleanTypeRef);
    }
}
