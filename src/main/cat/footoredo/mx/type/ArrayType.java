package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;
import cat.footoredo.mx.ast.TypeNode;
import cat.footoredo.mx.entity.Location;

import java.util.ArrayList;
import java.util.List;

public class ArrayType extends Type {
    private Type baseType;
    private int length;
    private static final int pointerSize = TypeTable.pointerSize;
    private static final int integerSize = TypeTable.integerSize;
    static final private int undefined = -1;

    public ArrayType(Type baseType, int length) {
        super ();/*
        FunctionType sizeFunc = new FunctionType(null, new IntegerType(), new ParamTypes(new ArrayList<>()));
        Slot size = new Slot(new TypeNode(sizeFunc), "size");
        super.addMember(size);*/
        this.baseType = baseType;
        this.length = length;
    }

    public ArrayType(Type baseType) {
        this(baseType, undefined);
    }

    public Type getBaseType() {
        return baseType;
    }

    /* public ArrayType(int length) {
        this.length = length;
    }*/

    public String toString() {
        if (length == undefined) {
            return baseType.toString() + "[]";
        }
        else {
            return baseType.toString() + "[" + length + "]";
        }
    }

    @Override
    public int size() {
        return pointerSize;
    }

    @Override
    public int allocateSize() {
        if (length == undefined) {
            return 0;
        }
        else {
            return integerSize + baseType.size() * length;
        }
    }

    public String getName() {
        return "__array";
    }

    @Override
    public MemberType getMemberType() {
        return ProtoArrayType.protoArrayTypeInstance;
    }

    @Override
    public boolean isScaler() {
        return false;
    }
}
