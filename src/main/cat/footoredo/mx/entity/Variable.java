package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.TypeNode;
import cat.footoredo.mx.ast.VariableDeclarationNode;
import cat.footoredo.mx.type.VoidTypeRef;

public class Variable extends Entity {
    protected VariableDeclarationNode variableDeclarationNode;
    public Variable (VariableDeclarationNode variableDeclarationNode) {
        super ();
        this.variableDeclarationNode = variableDeclarationNode;
    }
}
