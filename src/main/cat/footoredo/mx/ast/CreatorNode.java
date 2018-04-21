package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.ArrayTypeRef;
import cat.footoredo.mx.type.TypeRef;
import cat.footoredo.mx.type.ClassTypeRef;

import java.util.List;

public class CreatorNode extends Node {
    protected Location location;
    protected TypeNode type;
    protected List<ExpressionNode> lengths;
    protected List<ExpressionNode> args;

    public CreatorNode (Location location, TypeRef typeRef) {
        super ();
        this.location = location;
        this.type = new TypeNode(typeRef);
        this.lengths = null;
        this.args = null;
    }

    public CreatorNode (Location location, ArrayTypeRef arrayTypeRef, List<ExpressionNode> lengths) {
        super ();
        this.location = location;
        this.type = new TypeNode(arrayTypeRef);
        this.lengths = lengths;
        this.args = null;
        // System.out.println ("new " + type.toString() + " (" + Integer.toString(lengths.size ()) + " dim known) @ " + location.toString());
    }

    public CreatorNode(Location location, ClassTypeRef classTypeRef, List<ExpressionNode> args) {
        super ();
        this.location = location;
        this.type = new TypeNode(classTypeRef);
        this.lengths = null;
        this.args = args;
    }

    public Location getLocation() {
        return location;
    }

    public List<ExpressionNode> getArgs() {
        return args;
    }

    public List<ExpressionNode> getLengths() {
        return lengths;
    }

    public boolean hasArgs() { return args != null; }

    public boolean hasLengths() { return lengths != null; }

    public TypeNode getType() {
        return type;
    }
}
