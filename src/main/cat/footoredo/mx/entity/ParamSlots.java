package cat.footoredo.mx.entity;

import java.util.ArrayList;
import java.util.List;

public abstract class ParamSlots<T> {
    protected List<T> paramDescriptors;

    public ParamSlots (List <T> paramDescriptors) {
        this.paramDescriptors = paramDescriptors;
    }
    public ParamSlots () { this.paramDescriptors = new ArrayList<>(); }

    public void addParamDescriptor (T paramDescriptor) {
        this.paramDescriptors.add (paramDescriptor);
    }

    public List<T> getParamDescriptors() {
        return paramDescriptors;
    }

    public int size () {
        return paramDescriptors.size ();
    }
}
