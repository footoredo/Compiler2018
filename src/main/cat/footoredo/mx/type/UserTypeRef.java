package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class UserTypeRef extends TypeRef {
    protected String name;

    public UserTypeRef (Location location, String name) {
        super (location);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
