package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ForNode extends StatementNode {
    protected Location location;
    private ExprNode init, judge, update;
    private StatementNode body;

    ForNode (Location location, ExprNode init, ExprNode judge, ExprNode update, StatementNode body) {
        super ();
        this.location = location;
        this.init = init;
        this.judge = judge;
        this.update = update;
        this.body = body;

        System.out.println("For statement @ " + location.toString());
    }

    public Location getLocation() {
        return location;
    }

    public ExprNode getInit() {
        return init;
    }

    public ExprNode getJudge() {
        return judge;
    }

    public ExprNode getUpdate() {
        return update;
    }

    public StatementNode getBody() {
        return body;
    }
}
