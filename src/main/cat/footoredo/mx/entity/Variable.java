package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.ExpressionNode;
import cat.footoredo.mx.ast.TypeNode;
import cat.footoredo.mx.ast.VariableDeclarationNode;

public class Variable extends Entity {
    protected ExpressionNode initializer;

    public Variable (VariableDeclarationNode variableDeclarationNode) {
        super (variableDeclarationNode.getTypeNode(), variableDeclarationNode.getName());
        this.initializer = variableDeclarationNode.getInitExpr();
    }

    public Variable (TypeNode type, String name) {
        super (type, name);
        this.initializer = null;
    }

    public ExpressionNode getInitializer() {
        return initializer;
    }
}
