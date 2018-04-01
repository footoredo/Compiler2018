package cat.footoredo.mx.ast;

import java.util.Set;

public class AST extends Node {
    protected Declarations declarations;

    public AST (Declarations declarations) {
        super ();
        this.declarations = declarations;
    }
}
