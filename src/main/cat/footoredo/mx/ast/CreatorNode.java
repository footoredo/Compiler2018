package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.ArrayTypeRef;
import cat.footoredo.mx.type.TypeRef;
import cat.footoredo.mx.type.UserTypeRef;

import java.util.List;

public class CreatorNode extends ExpressionNode {
    protected Location location;
    protected TypeNode type;
    protected List<ExpressionNode> length;
    protected List<ExpressionNode> args;

    public CreatorNode (Location location, TypeRef typeRef) {
        super ();
        this.location = location;
        this.type = new TypeNode(typeRef);
        this.length = null;
        this.args = null;
    }

    public CreatorNode (Location location, ArrayTypeRef arrayTypeRef, List<ExpressionNode> length) {
        super ();
        this.location = location;
        this.type = new TypeNode(arrayTypeRef);
        this.length = length;
        this.args = null;
        System.out.println ("new " + type.toString() + " (" + Integer.toString(length.size ()) + " dim known) @ " + location.toString());
    }

    public CreatorNode(Location location, UserTypeRef userTypeRef, List<ExpressionNode> args) {
        super ();
        this.location = location;
        this.type = new TypeNode(userTypeRef);
        this.length = null;
        this.args = args;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public List<ExpressionNode> getArgs() {
        return args;
    }

    public List<ExpressionNode> getLength() {
        return length;
    }

    public TypeNode getType() {
        return type;
    }
}
