package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.IntegerTypeRef;

public class IntegerLiteralNode extends LiteralNode {
    protected int value;

    public IntegerLiteralNode (Location location, int value) {
        super (location, new IntegerTypeRef());
        this.value = value;
    }

    public int getValue () {
        return value;
    }

    public String toString () {
        return "[int " + Integer.toString(value) + "]";
    }
}
