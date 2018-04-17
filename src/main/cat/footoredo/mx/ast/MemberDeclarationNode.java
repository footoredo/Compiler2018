package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

abstract public class MemberDeclarationNode extends Node {
    abstract public Location getLocation ();
}
