package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.ParamSlots;

import java.util.List;

public class ParamTypeRefs extends ParamSlots <TypeRef> {
    public ParamTypeRefs (List<TypeRef> paramDescriptors) {
        super (paramDescriptors);
    }

    public List<TypeRef> getTypeRefs () {
        return paramDescriptors;
    }
}
