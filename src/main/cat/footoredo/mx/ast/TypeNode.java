package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.TypeRef;

public class TypeNode extends Node {
    TypeRef typeRef;

    public TypeNode (TypeRef typeRef) {
        super ();
        this.typeRef = typeRef;
    }

    public TypeRef getTypeRef() {
        return typeRef;
    }

    public Location getLocation () {
        return typeRef.getLocation ();
    }
}
