package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.BlockNode;
import cat.footoredo.mx.ast.MethodDescriptionNode;
import cat.footoredo.mx.ast.MethodNode;
import cat.footoredo.mx.ast.TypeNode;
import cat.footoredo.mx.type.Type;

import java.lang.reflect.Method;
import java.util.List;

abstract public class Function extends Entity {
    private Params params;
    private TypeNode returnType;
    public Function (TypeNode typeNode, MethodDescriptionNode methodDescriptionNode) {
        super (typeNode, methodDescriptionNode.getName());
        this.returnType = methodDescriptionNode.getReturnType();
        this.params = methodDescriptionNode.getParams();

    }

    public Function(TypeNode typeNode, String name, Params params) {
        super(typeNode, name);
        this.params = params;
    }

    public List<Parameter> getParameters() {
        return params.getParamDescriptors();
    }

    public TypeNode getReturnTypeNode() {
        return returnType;
    }

    public Type getReturnType() {return returnType.getType();}
}
