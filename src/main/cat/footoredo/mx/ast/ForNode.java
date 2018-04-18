package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class ForNode extends StatementNode {
    protected Location location;
    private ExpressionNode init, judge, update;
    private StatementNode body;

    ForNode (Location location, ExpressionNode init, ExpressionNode judge, ExpressionNode update, StatementNode body) {
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

    public ExpressionNode getInit() {
        return init;
    }

    public ExpressionNode getJudge() {
        return judge;
    }

    public ExpressionNode getUpdate() {
        return update;
    }

    public StatementNode getBody() {
        return body;
    }

    public boolean hasInit() { return init != null; }

    public boolean hasJudge() { return judge != null; }

    public boolean hasUpdate() { return update != null; }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
