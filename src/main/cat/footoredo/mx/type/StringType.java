package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;

import java.util.List;

public class StringType extends MemberType {
    private static final int undefined = -1;
    private static final int pointerSize = TypeTable.pointerSize;
    private static final int charSize = TypeTable.charSize;
    private int length;
    public StringType(List<Slot> members) {
        super(members);
        this.length = undefined;
    }

    @Override
    public int size() {
        return pointerSize;
    }

    @Override
    public int allocateSize() {
        if (length == undefined) {
            return 0;
        }
        else {
            return charSize * (length + 1);
        }
    }

    public String toString () {
        return "string";
    }

    @Override
    public String getName() {
        return "string";
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
