package cat.footoredo.mx.entity;

import java.util.List;

public class Params extends ParamSlots<Parameter> {
    public Params (List<Parameter> paramDescs) {
        super (paramDescs);
    }

    public Params () {
        super ();
    }

    public List <Parameter> getParams () {
        return paramDescriptors;
    }
}
