package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.NullTypeRef;

public class NullLiteralNode extends LiteralNode {
    public NullLiteralNode(Location location) {
        super(location, new NullTypeRef(location));
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
