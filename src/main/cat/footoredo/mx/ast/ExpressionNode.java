package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

abstract public class ExpressionNode extends Node {
    public String toString () {
        return "";
    }
    abstract Location getLocation();

    abstract public <S,E> E accept(ASTVisitor<S,E> visitor);
}
