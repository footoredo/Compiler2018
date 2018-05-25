package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class ProtoArrayTypeRef extends BuiltinTypeRef {
    public ProtoArrayTypeRef() {
        super(null);
    }

    @Override
    public String toString() {
        return "__array";
    }
}
