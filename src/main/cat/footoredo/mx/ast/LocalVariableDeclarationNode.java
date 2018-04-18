package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class LocalVariableDeclarationNode extends StatementNode {
    private TypeNode typeNode;
    private String name;
    private ExpressionNode initExpr;

    LocalVariableDeclarationNode(TypeNode typeNode, String name, ExpressionNode initExpr) {
        super ();
        this.typeNode = typeNode;
        this.name = name;
        this.initExpr = initExpr;
    }

    LocalVariableDeclarationNode(TypeNode typeNode, String name) {
        super ();
        this.typeNode = typeNode;
        this.name = name;
        this.initExpr = null;
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

    @Override
    public <S,E> S accept(ASTVisitor <S,E> visitor) {
        visitor.visit(this);
    }
}
