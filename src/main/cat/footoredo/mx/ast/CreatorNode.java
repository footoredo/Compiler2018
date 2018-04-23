package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.ArrayTypeRef;
import cat.footoredo.mx.type.Type;
import cat.footoredo.mx.type.TypeRef;
import cat.footoredo.mx.type.ClassTypeRef;

import java.util.List;

public class CreatorNode extends Node {
    protected Location location;
    protected TypeNode typeNode;
    protected List<ExpressionNode> lengths;
    protected List<ExpressionNode> args;

    public CreatorNode (Location location, TypeRef typeNodeRef) {
        super ();
        this.location = location;
        this.typeNode = new TypeNode(typeNodeRef);
        this.lengths = null;
        this.args = null;
    }

    public CreatorNode (Location location, ArrayTypeRef arrayTypeRef, List<ExpressionNode> lengths) {
        super ();
        this.location = location;
        this.typeNode = new TypeNode(arrayTypeRef);
        this.lengths = lengths;
        this.args = null;
        // System.out.println ("new " + typeNode.toString() + " (" + Integer.toString(lengths.size ()) + " dim known) @ " + location.toString());
    }

    public CreatorNode(Location location, ClassTypeRef classTypeRef, List<ExpressionNode> args) {
        super ();
        this.location = location;
        this.typeNode = new TypeNode(classTypeRef);
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

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public Type getType() {
        return typeNode.getType();
    }
}
