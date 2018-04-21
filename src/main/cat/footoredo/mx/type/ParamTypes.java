package cat.footoredo.mx.type;

import cat.footoredo.mx.entity.ParamSlots;

import java.util.List;

public class ParamTypes extends ParamSlots<Type> {
    protected ParamTypes(List<Type> paramDescs) {
        super(paramDescs);
    }

    public List<Type> getTypes() {
        return paramDescriptors;
    }
}
