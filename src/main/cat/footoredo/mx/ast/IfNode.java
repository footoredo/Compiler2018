package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class IfNode extends StatementNode {
    protected Location location;
    private ExprNode judge;
    private StatementNode thenStatement;
    private StatementNode elseStatement;

    IfNode(Location location, ExprNode judge, StatementNode thenStatement) {
        super ();
        this.location = location;
        this.judge = judge;
        this.thenStatement = thenStatement;
        this.elseStatement = null;
    }

    IfNode(Location location, ExprNode judge, StatementNode thenStatement, StatementNode elseStatement) {
        super ();
        this.location = location;
        this.judge = judge;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;

        System.out.println("If statement with else @ " + location.toString());
    }

    public Location getLocation() {
        return location;
    }

    public ExprNode getJudge() {
        return judge;
    }

    public StatementNode getThenStatement() {
        return thenStatement;
    }

    public StatementNode getElseStatement() {
        return elseStatement;
    }
}
