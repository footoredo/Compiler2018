package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.ConstantEntry;
import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.StringTypeRef;
import cat.footoredo.mx.utils.StringUtils;

public class StringLiteralNode extends LiteralNode {
    private String value;
    private ConstantEntry entry;

    public StringLiteralNode (Location location, String value) {
        super (location, new StringTypeRef());
        // System.out.println (value + value.length());
        this.value = StringUtils.unescape(value);
        // System.out.println (this.value.length() + " " + this.value);
    }

    public ConstantEntry getEntry() {
        return entry;
    }

    public void setEntry(ConstantEntry entry) {
        this.entry = entry;
    }

    public String getValue () {
        return value;
    }

    public String toString () {
        return "[string " + value + "]";
    }

    @Override
    public <S,E> E accept(ASTVisitor <S,E> visitor) {
        return visitor.visit(this);
    }
}
