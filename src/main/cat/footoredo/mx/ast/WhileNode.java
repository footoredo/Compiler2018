package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class WhileNode extends StatementNode {
    private Location location;
    private ExprNode judge;
    private StatementNode body;

    public WhileNode(Location location, ExprNode judge, StatementNode body) {
        super ();
        this.location = location;
        this.judge = judge;
        this.body = body;
    }

    public Location getLocation() {
        return location;
    }

    public ExprNode getJudge() {
        return judge;
    }

    public StatementNode getBody() {
        return body;
    }
}
