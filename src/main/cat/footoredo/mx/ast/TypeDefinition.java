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

    @Override
    public boolean isMemorable() {
        throw new Error ("querying TypeDefinition#isMemorable");
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    TypeDefinition(TypeNode typeNode) {
        super ();
        this.typeNode = typeNode;
    }

    public String getName() {
        return typeNode.getTypeRef().toString();
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
