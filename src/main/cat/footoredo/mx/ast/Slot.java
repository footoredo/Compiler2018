package cat.footoredo.mx.ast;

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

    public String getName() {
        return name;
    }
}
