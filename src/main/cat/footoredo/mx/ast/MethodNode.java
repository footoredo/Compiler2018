package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.entity.Parameter;
import cat.footoredo.mx.entity.Params;
import cat.footoredo.mx.type.FunctionTypeRef;
import cat.footoredo.mx.type.ParamTypeRefs;
import cat.footoredo.mx.type.TypeRef;

import java.util.ArrayList;
import java.util.List;

public class MethodNode extends Node {
    private MethodDescriptionNode methodDescription;
    private BlockNode block;
    private TypeNode typeNode;

    MethodNode (MethodDescriptionNode methodDescription, BlockNode block) {
        super ();
        this.methodDescription = methodDescription;
        this.block = block;
        List<TypeRef> paramTypeRefs = new ArrayList<>();
        for (Parameter parameter: methodDescription.getParams().getParams()) {
            paramTypeRefs.add(parameter.getTypeNode().getTypeRef());
        }
        this.typeNode = new TypeNode(new FunctionTypeRef(methodDescription.getLocation(),
                methodDescription.getReturnType().getTypeRef(), new ParamTypeRefs(paramTypeRefs)));

        // System.out.println( "new function " + name + " with " + Integer.toString(parameterNodes.size()) +
        //        " params and return type " + type.toString() + " @ " + type.getLocation().toString() );
    }

    public TypeNode getReturnType() {
        return methodDescription.getReturnType();
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

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public MethodDescriptionNode getMethodDescription() {
        return methodDescription;
    }
}
