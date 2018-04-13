package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.IntegerLiteralNode;
import cat.footoredo.mx.entity.Location;

public class IntegerTypeRef extends TypeRef {
    public IntegerTypeRef (Location location) {
        super (location);
    }
    public String toString() {
        return "int";
    }
}
