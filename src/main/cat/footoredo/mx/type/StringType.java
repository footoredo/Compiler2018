package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;

import java.util.List;

public class StringType extends MemberType {
    private int pointerSize;
    public StringType(List<Slot> members, int pointerSize) {
        super(members);
        this.pointerSize = pointerSize;
    }

    @Override
    public int size() {
        return pointerSize;
    }

    public String toString () {
        return "string";
    }
}
