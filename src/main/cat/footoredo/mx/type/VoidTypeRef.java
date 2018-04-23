package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class VoidTypeRef extends BuiltinTypeRef implements SelfDefiningType  {
    public VoidTypeRef (Location location) {
        super (location);
    }
    public VoidTypeRef () {super (null);}
    public String toString () {
        return "void";
    }

    public Type definingType() {
        return new VoidType();
    }

    public boolean equals(Object other) {
        return (other instanceof VoidTypeRef);
    }
}
