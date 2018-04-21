package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.BlockNode;
import cat.footoredo.mx.ast.MethodNode;

public class DefinedFunction extends Function {
    BlockNode block;
    LocalScope scope;

    public DefinedFunction (MethodNode methodNode) {
        super (methodNode.getMethodDescription());
        this.block = methodNode.getBlock();
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
