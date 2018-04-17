package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ReturnNode extends StatementNode {
    private Location location;
    private ExpressionNode expr;

    ReturnNode(Location location, ExpressionNode expr) {
        super ();
        this.location = location;
        this.expr = expr;
    }

    ReturnNode(Location location) {
        super ();
        this.location = location;
        this.expr = null;
    }

    public Location getLocation() {
        return location;
    }

    public ExpressionNode getExpr() {
        return expr;
    }
}
