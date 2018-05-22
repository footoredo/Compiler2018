package cat.footoredo.mx.type;

public class BooleanType extends Type {
    private int size;

    public BooleanType(int size) {
        this.size = size;
    }

    public String toString() {
        return "bool";
    }

    @Override
    public int size() {
        return size;
    }
}
