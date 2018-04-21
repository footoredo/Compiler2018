package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;
import cat.footoredo.mx.entity.Location;

import java.util.List;

public class ClassType extends Type {
    private String name;
    protected Location location;
    private List<Slot> members;

    public ClassType(Location location, String name, List<Slot> members) {
        super ();
        this.location = location;
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public List<Slot> getMembers() {
        return members;
    }
}
