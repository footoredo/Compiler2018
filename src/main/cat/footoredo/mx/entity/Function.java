package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.BlockNode;
import cat.footoredo.mx.ast.MethodDescriptionNode;
import cat.footoredo.mx.ast.MethodNode;
import cat.footoredo.mx.ast.TypeNode;

import java.util.List;

abstract public class Function extends Entity {
    private Params params;
    public Function (MethodDescriptionNode methodDescriptionNode) {
        super (methodDescriptionNode.getType(), methodDescriptionNode.getName());
        this.params = methodDescriptionNode.getParams();

    }

    public Function(TypeNode typeNode, String name, Params params) {
        super(typeNode, name);
        this.params = params;
    }

    public List<Parameter> getParameters() {
        return params.getParamDescriptors();
    }
}
