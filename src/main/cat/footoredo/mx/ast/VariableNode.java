package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Entity;
import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.Type;

public class VariableNode extends LHSNode {
    protected Location location;
    protected String name;
    private Entity entity;
    private boolean isMember;

    public VariableNode (Location location, String name) {
        super ();
        this.location = location;
        this.name = name;
        this.isMember = false;
    }

    public String getName () {
        return name;
    }

    public Entity getEntity() {
        if (entity == null)
            throw new Error(name + " has no entity.");
        return entity;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
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

    @Override
    public Type getType() {
        // System.out.println("asdasd");
        // System.out.println(entity.getTypeNode().toString());
        return entity.getType();
    }
}
