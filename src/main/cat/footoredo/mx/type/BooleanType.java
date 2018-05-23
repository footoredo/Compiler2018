package cat.footoredo.mx.type;

public class BooleanType extends Type {
    private static final int size = TypeTable.booleanSize;

    public BooleanType() {}

    public String toString() {
        return "bool";
    }

    @Override
    public int size() {
        return size;
    }
}
