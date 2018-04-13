package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class IntegerLiteralNode extends LiteralNode {
    protected int value;

    public IntegerLiteralNode (int value) {
        super ();
        this.value = value;
    }

    public int getValue () {
        return value;
    }
}
