package cat.footoredo.mx.ast;

import java.util.List;

public abstract class ParamSlots<T> {
    protected List<T> paramDescriptors;

    public ParamSlots (List <T> paramDescriptors) {
        this.paramDescriptors = paramDescriptors;
    }

    public int size () {
        return paramDescriptors.size ();
    }
}
