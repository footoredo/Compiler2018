package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;
import cat.footoredo.mx.entity.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassType extends MemberType {
    private String name;
    protected Location location;
    private int pointerSize;

    public ClassType(Location location, String name, List<Slot> members) {
        super (members);
        this.location = location;
        this.name = name;
    }

    public String toString() {return "[class " + name + "]";}

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public int size() {
        return pointerSize;
    }
}
