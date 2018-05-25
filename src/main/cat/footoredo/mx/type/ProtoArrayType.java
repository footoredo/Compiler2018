package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;

import java.util.List;

public class ProtoArrayType extends MemberType {
    public static ProtoArrayType protoArrayTypeInstance;
    public ProtoArrayType(List<Slot> members) {
        super(members);
        if (protoArrayTypeInstance != null)
            throw new Error ("proto array type instantiated more than once");
        protoArrayTypeInstance = this;
    }

    @Override
    public int size() {
        throw new Error ("requesting size of proto array type");
    }

    @Override
    public int allocateSize() {
        throw new Error ("requesting size of proto array type");
    }

    public String toString () {
        return getName();
    }

    @Override
    public String getName() {
        return "__array";
    }

    @Override
    public boolean isScaler() {
        return false;
    }
}
