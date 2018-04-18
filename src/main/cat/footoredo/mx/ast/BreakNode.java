package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class BreakNode extends StatementNode {
    protected Location location;

    public BreakNode(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

}
