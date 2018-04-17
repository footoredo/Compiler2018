package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class LocalVariableDeclarationNode extends BlockStatementNode {
    private TypeNode typeNode;
    private String name;
    private ExpressionNode initExpr;

    LocalVariableDeclarationNode(TypeNode typeNode, String name, ExpressionNode initExpr) {
        super ();
        this.typeNode = typeNode;
        this.name = name;
        this.initExpr = initExpr;

        System.out.println ("Local variable declaration: " + typeNode.toString() + " " + name + " = " + initExpr.toString() + " @ " + getLocation().toString());
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public Location getLocation() {
        return typeNode.getLocation();
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getInitExpr() {
        return initExpr;
    }
}
