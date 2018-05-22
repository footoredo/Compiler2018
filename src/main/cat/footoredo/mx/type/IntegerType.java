package cat.footoredo.mx.type;

public class IntegerType extends Type {
    private int size;

    public IntegerType(int size) {
        super ();
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }

    public String toString() {
        return "int";
    }
}
