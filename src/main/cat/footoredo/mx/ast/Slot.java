package cat.footoredo.mx.ast;

import cat.footoredo.mx.type.Type;

public class Slot extends Node {
    private TypeNode typeNode;
    private String name;

    public Slot(TypeNode typeNode, String name) {
        this.typeNode = typeNode;
        this.name = name;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public Type getType() {
        return typeNode.getType();
    }

    public String getName() {
        return name;
    }
}
