package cat.footoredo.mx.type;

public class IntegerType extends Type {
    private static final int size = TypeTable.integerSize;
    private boolean isSigned;

    public IntegerType(boolean isSigned) {
        this.isSigned = isSigned;
    }

    @Override
    public boolean isSigned() {
        return isSigned;
    }

    @Override
    public int size() {
        return size;
    }

    public String toString() {
        return "int";
    }
}
