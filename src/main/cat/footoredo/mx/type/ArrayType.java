package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;
import cat.footoredo.mx.ast.TypeNode;
import cat.footoredo.mx.entity.Location;

import java.util.ArrayList;
import java.util.List;

public class ArrayType extends MemberType {
    private Type baseType;
    private long length;
    static final private long undefined = -1;

    public ArrayType(Type baseType, long length) {
        super ();
        FunctionType sizeFunc = new FunctionType(null, new IntegerType(), new ParamTypes(new ArrayList<Type>()));
        Slot size = new Slot(new TypeNode(sizeFunc), "size");
        super.addMember(size);
        this.baseType = baseType;
        this.length = length;
    }

    public ArrayType(Type baseType) {
        this(baseType, undefined);
    }

    public Type getBaseType() {
        return baseType;
    }

    public ArrayType(long length) {
        this.length = length;
    }

    public String toString() {
        if (length == undefined) {
            return baseType.toString() + "[]";
        }
        else {
            return baseType.toString() + "[" + length + "]";
        }
    }
}
