package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.TypeRef;

abstract public class LiteralNode extends ExpressionNode {
    protected Location location;
    protected TypeNode typeNode;

    public LiteralNode(Location location, TypeRef typeRef) {
        super ();
        this.location = location;
        this.typeNode = new TypeNode(typeRef);
    }

    public Location getLocation() {
        return location;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }
}
