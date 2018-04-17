package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class MemberVariableDeclarationNode extends MemberDeclarationNode {
    private VariableDeclarationNode variableDeclarationNode;

    public MemberVariableDeclarationNode(VariableDeclarationNode variableDeclarationNode) {
        this.variableDeclarationNode = variableDeclarationNode;
    }

    @Override
    public Location getLocation() {
        return variableDeclarationNode.getLocation();
    }

    public VariableDeclarationNode getVariableDeclarationNode() {
        return variableDeclarationNode;
    }
}
