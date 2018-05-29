package cat.footoredo.mx.type;

public class PointerType extends Type {
    private static final int size = TypeTable.pointerSize;

    public PointerType() {}

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isSigned() {
        return false;
    }

    public String toString() {
        return "@ptr";
    }
}
