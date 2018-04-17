package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.BlockNode;
import cat.footoredo.mx.ast.MethodNode;
import cat.footoredo.mx.ast.TypeNode;

public class Function extends Entity {
    protected Params params;
    protected BlockNode block;
    public Function (MethodNode methodNode) {
        super (methodNode.getType(), methodNode.getName());
        this.params = methodNode.getParams();
        this.block = methodNode.getBlock();

    }

    public Params getParams() {
        return params;
    }

    public BlockNode getBlock() {
        return block;
    }
}
