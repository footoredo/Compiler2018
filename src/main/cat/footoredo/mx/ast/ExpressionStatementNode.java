package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ExpressionStatementNode extends StatementNode {
    private ExpressionNode expression;

    ExpressionStatementNode ( ExpressionNode expression) {
        super ();
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public Location getLocation () {
        return expression.getLocation();
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
