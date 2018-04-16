package cat.footoredo.mx.ast;

import cat.footoredo.mx.cst.MxParser;
import cat.footoredo.mx.entity.Location;

public class VariableDeclarationNode extends Node {
    private Location location;
    private TypeNode typeNode;
    private String name;
    private ExprNode initExpr;

    public VariableDeclarationNode(TypeNode typeNode, String name, ExprNode initExpr) {
        super ();
        this.typeNode = typeNode;
        this.location = typeNode.getLocation();
        this.name = name;
        this.initExpr = initExpr;

        System.out.println ("Variable declaration: " + typeNode.toString() + " " + name + " = " + initExpr.toString() + " @ " + location.toString());
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public ExprNode getInitExpr() {
        return initExpr;
    }
}
