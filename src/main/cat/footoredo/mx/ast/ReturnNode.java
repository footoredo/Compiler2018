package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ReturnNode extends StatementNode {
    private Location location;
    private ExpressionNode expression;

    ReturnNode(Location location, ExpressionNode expression) {
        super ();
        this.location = location;
        this.expression = expression;
    }

    ReturnNode(Location location) {
        super ();
        this.location = location;
        this.expression = null;
    }

    public Location getLocation() {
        return location;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public boolean hasExpression() { return expression != null; }

    @Override
    public <S,E> S accept(ASTVisitor <S,E> visitor) {
        return visitor.visit(this);
    }
}
