package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ConstructorDeclarationNode extends MemberDeclarationNode {
    private ConstructorNode constructorNode;

    public ConstructorDeclarationNode(ConstructorNode constructorNode) {
        this.constructorNode = constructorNode;
    }

    public ConstructorNode getConstructorNode() {
        return constructorNode;
    }

    @Override
    public Location getLocation() {
        return constructorNode.getLocation();
    }
}
