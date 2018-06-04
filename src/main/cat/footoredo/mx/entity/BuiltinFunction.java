package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.MethodDescriptionNode;
import cat.footoredo.mx.ast.MethodNode;
import cat.footoredo.mx.ast.TypeNode;

public class BuiltinFunction extends Function {
    private boolean isMemorable;
    public BuiltinFunction(MethodNode methodNode, String parentClass, boolean isMemorable) {
        super (methodNode.getTypeNode(), methodNode.getMethodDescription(), parentClass);
    }

    public boolean isMemorable() {
        return isMemorable;
    }

    public BuiltinFunction(MethodNode methodNode, boolean isMemorable) {
        this (methodNode, null, isMemorable);
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
