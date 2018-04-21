package cat.footoredo.mx.entity;

import java.util.Arrays;
import java.util.List;

public class Params extends ParamSlots<Parameter> {
    public Params (List<Parameter> paramDescs) {
        super (paramDescs);
    }

    public Params (Parameter... params) {
        paramDescriptors.addAll(Arrays.asList(params));
    }

    public Params () {
        super ();
    }

    public List <Parameter> getParams () {
        return paramDescriptors;
    }
}
