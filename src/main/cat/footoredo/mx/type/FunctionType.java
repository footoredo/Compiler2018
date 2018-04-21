package cat.footoredo.mx.type;

public class FunctionType extends Type {
    private Type returnType;
    private ParamTypes params;

    public FunctionType(Type returnType, ParamTypes params) {
        super ();
        this.returnType = returnType;
        this.params = params;
    }

    public Type getReturnType() {
        return returnType;
    }

    public ParamTypes getParams() {
        return params;
    }
}
