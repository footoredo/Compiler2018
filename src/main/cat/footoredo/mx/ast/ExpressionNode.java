package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.Type;

abstract public class ExpressionNode extends Node {
    abstract public Location getLocation();

    abstract public <S,E> E accept(ASTVisitor<S,E> visitor);

    abstract public Type getType();
}
