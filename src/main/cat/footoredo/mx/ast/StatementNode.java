package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

abstract public class StatementNode extends Node {
    abstract public Location getLocation();
    abstract public <S,E> S accept(ASTVisitor <S,E> visitor);
}
