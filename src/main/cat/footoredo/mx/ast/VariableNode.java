package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Entity;
import cat.footoredo.mx.entity.Location;

public class VariableNode extends LHSNode {
    protected Location location;
    protected String name;
    private Entity entity;

    public VariableNode (Location location, String name) {
        super ();
        this.location = location;
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
