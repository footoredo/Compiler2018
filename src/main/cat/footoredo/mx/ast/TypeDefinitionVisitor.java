package cat.footoredo.mx.ast;

public interface TypeDefinitionVisitor<T> {
    public T visit(ClassNode classNode);
    public T visit(BuiltinTypeNode builtinTypeNode);
}
