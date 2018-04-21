package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.entity.Params;

public class MethodNode extends Node {
    private MethodDescriptionNode methodDescription;
    private BlockNode block;

    MethodNode (MethodDescriptionNode methodDescription, BlockNode block) {
        super ();
        this.methodDescription = methodDescription;
        this.block = block;

        // System.out.println( "new function " + name + " with " + Integer.toString(parameterNodes.size()) +
        //        " params and return type " + type.toString() + " @ " + type.getLocation().toString() );
    }

    public TypeNode getType() {
        return methodDescription.getType();
    }

    public String getName() {
        return methodDescription.getName();
    }

    public Params getParams() {
        return methodDescription.getParams();
    }

    public BlockNode getBlock() {
        return block;
    }

    public Location getLocation () {
        return methodDescription.getLocation();
    }

    public MethodDescriptionNode getMethodDescription() {
        return methodDescription;
    }
}
