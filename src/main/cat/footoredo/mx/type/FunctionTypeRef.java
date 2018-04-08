package cat.footoredo.mx.type;

public class FunctionTypeRef extends TypeRef {
    protected TypeRef returnType;
    protected ParamTypeRefs params;

    public FunctionTypeRef (TypeRef returnType, ParamTypeRefs params) {
        super ();
        this.returnType = returnType;
        this.params = params;
    }

    public TypeRef getReturnType() {
        return returnType;
    }

    public ParamTypeRefs getParams() {
        return params;
    }
}
