package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ReturnNode extends StatementNode {
    private Location location;
    private ExprNode expr;

    ReturnNode(Location location, ExprNode expr) {
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

    public ExprNode getExpr() {
        return expr;
    }
}
