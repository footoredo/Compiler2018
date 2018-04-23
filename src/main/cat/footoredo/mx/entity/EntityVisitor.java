package cat.footoredo.mx.entity;

public interface EntityVisitor<T> {
    public T visit(DefinedFunction definedFunction);
    public T visit(BuiltinFunction builtinFunction);
    public T visit(Variable variable);
}
