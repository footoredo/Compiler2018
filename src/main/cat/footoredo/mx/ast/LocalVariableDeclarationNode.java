package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.entity.Variable;

public class LocalVariableDeclarationNode extends StatementNode {
    private Variable variable;
    private ExpressionNode initialExpression;

    LocalVariableDeclarationNode(TypeNode typeNode, String name, ExpressionNode initialExpression) {
        super ();
        this.variable = new Variable(typeNode, name);
        this.initialExpression = initialExpression;
    }

    LocalVariableDeclarationNode(TypeNode typeNode, String name) {
        super ();
        this.variable = new Variable(typeNode, name);
        this.initialExpression = null;
   }

    public TypeNode getTypeNode() {
        return variable.getTypeNode();
    }

    public Location getLocation() {
        return variable.getTypeNode().getLocation();
    }

    public String getName() {
        return variable.getName();
    }

    public ExpressionNode getInitialExpression() {
        return initialExpression;
    }

    public boolean hasInitialExpression () { return initialExpression != null; }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public <S,E> S accept(ASTVisitor <S,E> visitor) {
        return visitor.visit(this);
    }
}
