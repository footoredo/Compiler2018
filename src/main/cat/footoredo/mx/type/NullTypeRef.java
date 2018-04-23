package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class NullTypeRef extends BuiltinTypeRef implements SelfDefiningType {
    public NullTypeRef(Location location) {
        super(location);
    }
    public NullTypeRef() {
        super(null);
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public Type definingType() {
        return new NullType();
    }

    public boolean equals(Object other) {
        return (other instanceof NullTypeRef);
    }
}
