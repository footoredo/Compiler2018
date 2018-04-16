package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.BooleanTypeRef;

public class BooleanLiteralNode extends LiteralNode {
    protected boolean value;

    public BooleanLiteralNode (Location location, boolean value) {
        super (location, new BooleanTypeRef());
        this.value = value;
    }

    public boolean getValue () {
        return value;
    }

    public String toString () {
        return "[bool " + (value ? "true" : "false") + "]";
    }
}
