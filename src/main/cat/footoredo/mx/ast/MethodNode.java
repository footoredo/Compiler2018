package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.entity.Parameter;
import cat.footoredo.mx.entity.Params;

import java.util.List;

public class MethodNode extends Node {
    protected TypeNode type;
    protected String name;
    protected Params params;
    protected BlockNode block;
    protected Location location;

    public MethodNode (TypeNode type, String name, List<ParameterNode> parameterNodes, BlockNode block) {
        super ();
        this.type = type;
        this.name = name;
        this.block = block;
        this.params = new Params();
        for (ParameterNode parameterNode: parameterNodes) {
            Parameter parameter = new Parameter(parameterNode);
            this.params.addParamDescriptor(parameter);
        }
        this.location = type.getLocation();

        // System.out.println( "new function " + name + " with " + Integer.toString(parameterNodes.size()) +
        //        " params and return type " + type.toString() + " @ " + type.getLocation().toString() );
    }

    public MethodNode (Location location, String name, List<ParameterNode> parameterNodes, BlockNode block) {
        super ();
        this.location = location;
        this.type = null;
        this.name = name;
        this.block = block;
        this.params = new Params();
        for (ParameterNode parameterNode: parameterNodes) {
            Parameter parameter = new Parameter(parameterNode);
            this.params.addParamDescriptor(parameter);
        }

        // System.out.println( "new function " + name + " with " + Integer.toString(parameterNodes.size()) +
        //        " params and return type " + type.toString() + " @ " + type.getLocation().toString() );
    }

    public TypeNode getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Params getParams() {
        return params;
    }

    public BlockNode getBlock() {
        return block;
    }

    public Location getLocation () {
        return location;
    }
}
