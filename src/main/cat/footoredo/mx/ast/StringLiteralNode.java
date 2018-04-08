package cat.footoredo.mx.ast;

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
