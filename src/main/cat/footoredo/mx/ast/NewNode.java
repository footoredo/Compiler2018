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

    public List<ExpressionNode> getLength () {
        return creator.getLength();
    }

    public List<ExpressionNode> getArgs () {
        return creator.getArgs();
    }
}
