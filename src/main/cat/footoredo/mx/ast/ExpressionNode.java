package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.MemberType;
import cat.footoredo.mx.type.Type;

abstract public class ExpressionNode extends Node {
    abstract public Location getLocation();

    abstract public <S,E> E accept(ASTVisitor<S,E> visitor);

    abstract public Type getType();

    public MemberType getMemberType () {
        return getType().getMemberType();
    }

    public boolean isLoadable () {
        return false;
    }
}
