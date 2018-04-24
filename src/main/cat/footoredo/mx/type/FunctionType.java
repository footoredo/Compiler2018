package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.Location;

public class FunctionType extends Type {
    private Type returnType;
    private ParamTypes params;
    private Location location;

    public FunctionType(Location location, Type returnType, ParamTypes params) {
        super ();
        this.returnType = returnType;
        this.params = params;
        this.location = location;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append (returnType.toString ());
        buf.append (" (");
        boolean isFirst = true;
        for (Type ref : this.params.getTypes()) {
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

    public Type getReturnType() {
        return returnType;
    }

    public ParamTypes getParams() {
        return params;
    }

    public int getArgc() {
        return params.size();
    }
}
