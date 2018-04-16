package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.ExprNode;
import cat.footoredo.mx.ast.TypeNode;
import cat.footoredo.mx.ast.VariableDeclarationNode;
import cat.footoredo.mx.type.VoidTypeRef;

public class Variable extends Entity {
    protected ExprNode initializer;
    protected TypeNode type;
    protected String name;

    public Variable (VariableDeclarationNode variableDeclarationNode) {
        super ();
        this.type = variableDeclarationNode.getTypeNode();
        this.name = variableDeclarationNode.getName();
        this.initializer = variableDeclarationNode.getInitExpr();
    }

    public Variable (TypeNode type, String name) {
        this.type = type;
        this.name = name;
        this.initializer = null;
    }

    public ExprNode getInitializer() {
        return initializer;
    }

    public TypeNode getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }
}
