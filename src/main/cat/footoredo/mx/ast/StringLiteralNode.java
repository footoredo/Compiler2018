package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.StringTypeRef;

public class StringLiteralNode extends LiteralNode {
    protected String value;

    public StringLiteralNode (Location location, String value) {
        super (location, new StringTypeRef());
        this.value = value;
    }

    public String getValue () {
        return value;
    }

    public String toString () {
        return "[string " + value + "]";
    }

    @Override
    public <S,E> E accept(ASTVisitor <S,E> visitor) {
        visitor.visit(this);
    }
}
