package cat.footoredo.mx.ast;

public class BuiltinDescriptionNode extends Node {
    private boolean isMemorable;
    private MethodDescriptionNode descriptionNode;

    public BuiltinDescriptionNode(boolean isMemorable, MethodDescriptionNode descriptionNode) {
        this.isMemorable = isMemorable;
        this.descriptionNode = descriptionNode;
    }

    @Override
    public boolean isMemorable() {
        return isMemorable;
    }

    public MethodDescriptionNode getDescriptionNode() {
        return descriptionNode;
    }
}
