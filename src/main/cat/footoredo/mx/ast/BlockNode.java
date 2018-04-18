package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.LocalScope;
import cat.footoredo.mx.entity.Location;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends StatementNode {
    protected Location location;
    private List<StatementNode> statements;
    private LocalScope scope;

    BlockNode (Location location) {
        super ();
        this.location = location;
        this.statements = new ArrayList<>();
    }

    public void addStatement (StatementNode statement) {
        statements.add (statement);
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    public Location getLocation() {
        return location;
    }

    public LocalScope getScope() {
        return scope;
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
