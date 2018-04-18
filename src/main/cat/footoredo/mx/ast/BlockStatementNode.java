package cat.footoredo.mx.ast;

abstract public class BlockStatementNode extends Node {
    abstract public <S,E> S accept(ASTVisitor<S,E> visitor);
}
