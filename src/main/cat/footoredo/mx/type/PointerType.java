package cat.footoredo.mx.type;

public class PointerType extends Type {
    private static final int size = TypeTable.pointerSize;

    public PointerType() {}

    @Override
    public int size() {
        return size;
    }

    public String toString() {
        return "@ptr";
    }
}
