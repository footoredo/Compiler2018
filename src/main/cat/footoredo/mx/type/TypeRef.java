package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public abstract class TypeRef {
    protected Location location;
    public TypeRef (Location location) {
        this.location = location;
    }
    public abstract String toString ();

    public Location getLocation() {
        return location;
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
