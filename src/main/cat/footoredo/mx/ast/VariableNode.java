package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class VariableNode extends LHSNode {
    protected Location location;
    protected String name;

    public VariableNode (Location location, String name) {
        super ();
        this.location = location;
        this.name = name;
    }

    public String getName () {
        return name;
    }

    @Override
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
