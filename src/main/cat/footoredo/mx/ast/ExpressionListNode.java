package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

import java.util.List;

public class ExpressionListNode extends Node {
    private Location location;
    private List<ExprNode> exprs;

    public ExpressionListNode(Location location, List<ExprNode> exprs) {
        super ();
        this.location = location;
        this.exprs = exprs;
    }

    public List<ExprNode> getExprs() {
        return exprs;
    }

    public Location getLocation() {
        return location;
    }
}
