package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.TypeNode;

abstract public class Entity {
    protected TypeNode typeNode;
    protected String name;
    private long referredCount;

    public Entity (TypeNode typeNode, String name) {
        this.typeNode = typeNode;
        this.name = name;
        this.referredCount = 0;
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

    public void referred () {
        referredCount ++;
    }
}
