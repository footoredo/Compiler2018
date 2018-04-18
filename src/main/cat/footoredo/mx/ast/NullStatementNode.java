package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class NullStatementNode extends StatementNode {
    protected Location location;

    public NullStatementNode(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public <S,E> S accept(ASTVisitor <S,E> visitor) {
        visitor.visit(this);
    }
}
