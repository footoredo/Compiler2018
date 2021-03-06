package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class FunctionTypeRef extends TypeRef {
    protected TypeRef returnType;
    protected ParamTypeRefs params;
    private Location location;

    public FunctionTypeRef (Location location, TypeRef returnType, ParamTypeRefs params) {
        super (location);
        this.returnType = returnType;
        this.params = params;
    }

    public TypeRef getReturnType() {
        return returnType;
    }

    public ParamTypeRefs getParams() {
        return params;
    }

    public String toString () {
        StringBuffer buf = new StringBuffer();
        buf.append (returnType.toString ());
        buf.append (" (");
        boolean isFirst = true;
        for (TypeRef ref : this.params.getTypeRefs()) {
            if (!isFirst) buf.append (", ");
            isFirst = false;
            buf.append (ref.toString());
        }
        buf.append (") @");
        if (location != null)
            buf.append (location.toString());
        else
            buf.append ("__builtin");
        return buf.toString();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public boolean equals(Object other) {
        return (other instanceof FunctionTypeRef);
    }
}
