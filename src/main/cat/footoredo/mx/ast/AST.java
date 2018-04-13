package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

import java.util.Set;

public class AST extends Node {
    protected Location location;
    protected Declarations declarations;

    public AST (Location location, Declarations declarations) {
        super ();
        this.location = location;
        this.declarations = declarations;
    }
}
