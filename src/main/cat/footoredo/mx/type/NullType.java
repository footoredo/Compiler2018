package cat.footoredo.mx.type;

public class NullType extends Type {
    @Override
    public int size() {
        return 0;
    }

    public String toString () {
        return "null";
    }
}
