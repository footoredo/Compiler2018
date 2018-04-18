package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.BlockNode;
import cat.footoredo.mx.ast.MethodNode;
import cat.footoredo.mx.ast.TypeNode;

import java.util.List;

public class Function extends Entity {
    protected Params params;
    protected BlockNode block;
    private LocalScope scope;
    public Function (MethodNode methodNode) {
        super (methodNode.getType(), methodNode.getName());
        this.params = methodNode.getParams();
        this.block = methodNode.getBlock();

    }

    public List<Parameter> getParameters() {
        return params.getParamDescriptors();
    }

    public BlockNode getBlock() {
        return block;
    }

    public LocalScope getScope() {
        return scope;
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }
}
