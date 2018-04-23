package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.LocalScope;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.type.Type;
import cat.footoredo.mx.type.TypeRef;

import java.util.ArrayList;
import java.util.List;

abstract public class TypeDefinition extends Node {
    private LocalScope scope;
    private TypeNode typeNode;

    public LocalScope getScope() {
        return scope;
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

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

    public List<Function> getMemberMethods() {
        return new ArrayList<>();
    }

    public List<Variable> getMemberVariables() {
        return new ArrayList<>();
    }
}
