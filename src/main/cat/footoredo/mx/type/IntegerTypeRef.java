package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.IntegerLiteralNode;
import cat.footoredo.mx.entity.Location;

public class IntegerTypeRef extends BuiltinTypeRef {
    public IntegerTypeRef (Location location) {
        super (location);
    }
    public IntegerTypeRef () {
        super (null);
    }
    public String toString() {
        return "int";
    }

    @Override
    public Type definingType() {
        return new IntegerType();
    }

    public boolean equals(Object other) {
        return (other instanceof IntegerTypeRef);
    }
}
