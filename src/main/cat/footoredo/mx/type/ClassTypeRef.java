package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class ClassTypeRef extends TypeRef {
    private String name;

    public ClassTypeRef(Location location, String name) {
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

    public boolean equals(Object other) {
        return (other instanceof ClassTypeRef);
    }
}
