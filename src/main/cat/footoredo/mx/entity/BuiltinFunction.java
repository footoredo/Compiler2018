package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.MethodDescriptionNode;
import cat.footoredo.mx.ast.MethodNode;
import cat.footoredo.mx.ast.TypeNode;

public class BuiltinFunction extends Function {
    public BuiltinFunction(MethodNode methodNode, String parentClass) {
        super (methodNode.getTypeNode(), methodNode.getMethodDescription(), parentClass);
    }

    public BuiltinFunction(MethodNode methodNode) {
        this (methodNode, null);
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
