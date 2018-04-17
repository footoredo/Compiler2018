package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.TypeNode;

abstract public class Entity {
    protected TypeNode typeNode;
    protected String name;

    public Entity (TypeNode typeNode, String name) {
        this.typeNode = typeNode;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public Location getLocation () {
        return typeNode.getLocation();
    }
}
