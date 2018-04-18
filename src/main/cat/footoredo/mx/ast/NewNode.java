package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

import java.util.List;

public class NewNode extends ExpressionNode {
    protected Location location;
    protected CreatorNode creator;

    public NewNode (Location location, CreatorNode creator) {
        super ();
        this.location = location;
        this.creator = creator;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public CreatorNode getCreator() {
        return creator;
    }

    public TypeNode getType () {
        return creator.getType();
    }

    public List<ExpressionNode> getLengths () {
        return creator.getLengths();
    }

    public List<ExpressionNode> getArgs () {
        return creator.getArgs();
    }

    public boolean hasArgs() { return creator.hasArgs(); }

    public boolean hasLengths() { return creator.hasLengths(); }

    @Override
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
