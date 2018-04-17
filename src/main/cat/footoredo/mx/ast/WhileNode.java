package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class WhileNode extends StatementNode {
    private Location location;
    private ExpressionNode judge;
    private StatementNode body;

    public WhileNode(Location location, ExpressionNode judge, StatementNode body) {
        super ();
        this.location = location;
        this.judge = judge;
        this.body = body;
    }

    public Location getLocation() {
        return location;
    }

    public ExpressionNode getJudge() {
        return judge;
    }

    public StatementNode getBody() {
        return body;
    }
}
