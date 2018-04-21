package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class IfNode extends StatementNode {
    protected Location location;
    private ExpressionNode judge;
    private StatementNode thenStatement;
    private StatementNode elseStatement;

    IfNode(Location location, ExpressionNode judge, StatementNode thenStatement) {
        super ();
        this.location = location;
        this.judge = judge;
        this.thenStatement = thenStatement;
        this.elseStatement = null;
    }

    IfNode(Location location, ExpressionNode judge, StatementNode thenStatement, StatementNode elseStatement) {
        super ();
        this.location = location;
        this.judge = judge;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;

        // System.out.println("If statement with else @ " + location.toString());
    }

    public Location getLocation() {
        return location;
    }

    public ExpressionNode getJudge() {
        return judge;
    }

    public StatementNode getThenStatement() {
        return thenStatement;
    }

    public StatementNode getElseStatement() {
        return elseStatement;
    }

    public boolean hasElseStatement() {return elseStatement != null;}

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
