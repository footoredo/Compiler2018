package cat.footoredo.mx.type;

import cat.footoredo.mx.exception.SemanticException;

import java.util.*;

public class TypeTable {
    private Map<TypeRef, Type> table;

    public TypeTable() {
        this.table = new HashMap<>();
    }

    public boolean isDefined(TypeRef typeRef) {
        return table.containsKey(typeRef);
    }

    public void put(TypeRef typeRef, Type type) {
        if (table.containsKey(typeRef)) {
            throw new SemanticException(typeRef.getLocation(), "duplicated type definition of " + typeRef.toString());
        }
        table.put(typeRef, type);
    }

    public Type get(TypeRef typeRef) {
        if (table.containsKey(typeRef)) {
            return table.get(typeRef);
        }
        else {
            Type type;
            /*if (typeRef instanceof ClassTypeRef) {
                ClassTypeRef classTypeRef = (ClassTypeRef) typeRef;
                throw new Error("undefined type: " + classTypeRef.getName());
            }
            else */if (typeRef instanceof ArrayTypeRef) {
                ArrayTypeRef arrayTypeRef = (ArrayTypeRef) typeRef;
                type = new ArrayType(get(arrayTypeRef.getBaseType()), arrayTypeRef.getLen());
            }
            else if (typeRef instanceof FunctionTypeRef) {
                FunctionTypeRef functionTypeRef = (FunctionTypeRef) typeRef;
                List<Type> paramTypes = new ArrayList<>();
                for (TypeRef paramTypeRef : functionTypeRef.getParams().getTypeRefs()) {
                    paramTypes.add(get(paramTypeRef));
                }
                type = new FunctionType(get(functionTypeRef.getReturnType()), new ParamTypes(paramTypes));
            }
            else {
                throw new Error("unregistered type: " + typeRef.toString());
            }
            table.put(typeRef, type);
            return type;
        }
    }

    public Collection<Type> getTypes() {
        return table.values();
    }

    public void semanticCheck() {
        for (Type t: getTypes()) {
            //if (t instanceof )
        }
    }
}
