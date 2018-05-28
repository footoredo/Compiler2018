package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;
import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.utils.AsmUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassType extends MemberType {
    private String name;
    protected Location location;
    private static final int pointerSize = TypeTable.pointerSize;
    private int cachedSize;

    public ClassType(Location location, String name, List<Slot> members) {
        super(members);
        this.location = location;
        this.name = name;
        this.cachedSize = Type.sizeUnknown;
    }

    public String toString() {
        return "[class " + name + "]";
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    private void computeOffsets() {
        int offset = 0;
        int maxSize = 1;
        for (Slot slot : getMembers()) {
            if (!slot.getType().isFunction()) {
                int size = slot.getType().size();
                if (size > maxSize) maxSize = size;
                offset = AsmUtils.align(offset, size);
                slot.setOffset(offset);
                offset += size;
            }
        }
        cachedSize = AsmUtils.align(offset, maxSize);
    }

    @Override
    public int size() {
        return pointerSize;
    }

    @Override
    public int allocateSize() {
        if (cachedSize == Type.sizeUnknown) {
            computeOffsets();
        }
        return cachedSize;
    }

    @Override
    public boolean isScaler() {
        return false;
    }

    @Override
    public MemberType getMemberType() {
        return this;
    }
}
