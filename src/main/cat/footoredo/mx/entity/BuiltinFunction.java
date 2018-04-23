package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.MethodDescriptionNode;

public class BuiltinFunction extends Function {
    public BuiltinFunction(MethodDescriptionNode methodDescriptionNode) {
        super (methodDescriptionNode);
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
