package cat.footoredo.mx.ast;

public interface TypeDefinitionVisitor<T> {
    public T visit(TypeDefinition typeDefinition);
    // public T visit(BuiltinTypeNode builtinTypeNode);
}
