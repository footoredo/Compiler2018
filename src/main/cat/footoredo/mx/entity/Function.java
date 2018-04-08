package cat.footoredo.mx.entity;

import cat.footoredo.mx.ast.MethodNode;

public class Function extends Entity {
    MethodNode methodNode;
    public Function (MethodNode methodNode) {
        super ();
        this.methodNode = methodNode;
    }
}
