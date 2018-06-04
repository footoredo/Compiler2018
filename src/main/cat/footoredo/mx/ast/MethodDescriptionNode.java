package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.entity.Parameter;
import cat.footoredo.mx.entity.Params;
import cat.footoredo.mx.type.VoidTypeRef;

import java.util.List;

public class MethodDescriptionNode extends Node {
    protected TypeNode returnType;
    protected String name;
    protected Params params;
    protected Location location;

    public MethodDescriptionNode(TypeNode returnType, String name, List<ParameterNode> parameterNodes) {
        super ();
        this.returnType = returnType;
        this.name = name;
        this.params = new Params();
        for (ParameterNode parameterNode: parameterNodes) {
            Parameter parameter = new Parameter(parameterNode);
            this.params.addParamDescriptor(parameter);
        }
        this.location = returnType.getLocation();

        // System.out.println( "new function " + name + " with " + Integer.toString(parameterNodes.size()) +
        //        " params and return type " + type.toString() + " @ " + type.getLocation().toString() );
    }

    public MethodDescriptionNode(Location location, String name, List<ParameterNode> parameterNodes) {
        super ();
        this.location = location;
        this.returnType = new TypeNode(new VoidTypeRef());
        this.name = name;
        this.params = new Params();
        for (ParameterNode parameterNode: parameterNodes) {
            Parameter parameter = new Parameter(parameterNode);
            this.params.addParamDescriptor(parameter);
        }

        // System.out.println( "new function " + name + " with " + Integer.toString(parameterNodes.size()) +
        //        " params and return type " + type.toString() + " @ " + type.getLocation().toString() );
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public Params getParams() {
        return params;
    }

    public Location getLocation () {
        return location;
    }

    @Override
    public boolean isMemorable() {
        throw new Error ("querying MethodDescriptionNode#isMemorable");
    }
}
