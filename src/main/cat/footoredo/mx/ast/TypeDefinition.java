package cat.footoredo.mx.ast;

import cat.footoredo.mx.type.Type;
import cat.footoredo.mx.type.TypeRef;

abstract public class TypeDefinition extends Node {
    private TypeNode typeNode;

    TypeDefinition(TypeNode typeNode) {
        super ();
        this.typeNode = typeNode;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public TypeRef getTypeRef() {
        return typeNode.getTypeRef();
    }

    public Type getType() {
        return typeNode.getType();
    }

    abstract public Type definingType();
    abstract public <T> T accept(TypeDefinitionVisitor<T> visitor);
}
