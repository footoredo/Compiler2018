package cat.footoredo.mx.ir;

import cat.footoredo.mx.entity.Location;

abstract public class Statement {
    private Location location;

    public Statement(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    abstract public <S, E> S accept (IRVisitor<S, E> visitor);
}
