package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class StringLiteralNode extends LiteralNode {
    protected String value;

    public StringLiteralNode (String value) {
        super ();
        this.value = value;
    }

    public String getValue () {
        return value;
    }
}
