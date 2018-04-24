package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;
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
        // System.out.println("Putting " + typeRef.toString() + " " + typeRef.hashCode());
        if (table.containsKey(typeRef)) {
            throw new SemanticException(typeRef.getLocation(), "duplicated type definition of " + typeRef.toString());
        }
        table.put(typeRef, type);
        // System.out.println(table.containsKey(typeRef));
    }

    public Type get(TypeRef typeRef) {
        // System.out.println("Getting " + typeRef.toString() + " " + typeRef.hashCode());
        // System.out.println(table.containsKey(typeRef));
        // System.out.println("in typeTable " + typeRef.toString());
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
                // System.out.println("in typeTable " + typeRef.toString());
                type = new FunctionType(functionTypeRef.getLocation(), get(functionTypeRef.getReturnType()), new ParamTypes(paramTypes));
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
            if (t instanceof ClassType) {
                checkVoidMembers((ClassType) t);
                checkDuplicatedMembers((ClassType) t);
            }
            else if (t instanceof ArrayType) {
                checkVoidMembers((ArrayType) t);
            }
        }
    }

    private void checkVoidMembers(ArrayType arrayType) {
        if (arrayType.getBaseType() instanceof VoidType)
            throw new SemanticException("got array of void");
    }

    private void checkVoidMembers(ClassType classType) {
        for (Slot slot: classType.getMembers()) {
            if (slot.getTypeNode().isVoid())
                throw new SemanticException("class " + classType.getName() + " has a void type member " + slot.getName());
        }
    }

    private void checkDuplicatedMembers(ClassType classType) {
        Set<String> pool = new HashSet<>();
        for (Slot slot: classType.getMembers()) {
            if (pool.contains(slot.getName())) {
                throw new SemanticException("class " + classType.getName() + "has duplicated member " + slot.getName());
            }
            pool.add(slot.getName());
        }
    }
}
