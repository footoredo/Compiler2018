package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class MemberMethodDeclarationNode extends MemberDeclarationNode {
    private MethodNode methodNode;

    public MemberMethodDeclarationNode(MethodNode methodNode) {
        this.methodNode = methodNode;
    }

    public MethodNode getMethodNode() {
        return methodNode;
    }

    @Override
    public Location getLocation() {
        return methodNode.getLocation();
    }
}
