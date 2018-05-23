package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.ExpressionNode;
import cat.footoredo.mx.ast.TypeNode;
import cat.footoredo.mx.ast.VariableDeclarationNode;
import cat.footoredo.mx.ir.Expression;
import cat.footoredo.mx.type.Type;

public class Variable extends Entity {
    private ExpressionNode initializer;
    private Expression ir;

    public Variable (VariableDeclarationNode variableDeclarationNode) {
        super (variableDeclarationNode.getTypeNode(), variableDeclarationNode.getName());
        this.initializer = variableDeclarationNode.getInitExpr();
    }

    public Variable (TypeNode type, String name) {
        super (type, name);
        this.initializer = null;
    }

    private static int tmpCounter = 0;

    static public Variable tmp (Type type) {
        return new Variable(new TypeNode(type), "@tmp" + tmpCounter ++);
    }

    public void setIr(Expression ir) {
        this.ir = ir;
    }

    public ExpressionNode getInitializer() {
        return initializer;
    }

    public boolean hasInitializer() { return initializer != null; }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
