package cat.footoredo.mx.type;

public class VoidType extends Type {
    public String toString () {
        return "void";
    }

    @Override
    public int size() {
        return 0;
    }
}
