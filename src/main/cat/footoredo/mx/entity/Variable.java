package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.VariableDeclarationNode;

public class Variable extends Entity {
    protected VariableDeclarationNode variableDeclarationNode;
    public Variable (VariableDeclarationNode variableDeclarationNode) {
        super ();
        this.variableDeclarationNode = variableDeclarationNode;
    }
}
