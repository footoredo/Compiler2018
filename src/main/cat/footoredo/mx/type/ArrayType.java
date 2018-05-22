package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;
import cat.footoredo.mx.ast.TypeNode;
import cat.footoredo.mx.entity.Location;

import java.util.ArrayList;
import java.util.List;

public class ArrayType extends MemberType {
    private Type baseType;
    private int length;
    private int pointerSize;
    static final private int undefined = -1;

    public ArrayType(Type baseType, int length, int pointerSize) {
        super ();
        FunctionType sizeFunc = new FunctionType(null, new IntegerType(), new ParamTypes(new ArrayList<>()));
        Slot size = new Slot(new TypeNode(sizeFunc), "size");
        super.addMember(size);
        this.baseType = baseType;
        this.length = length;
        this.pointerSize = pointerSize;
    }

    public ArrayType(Type baseType, int pointerSize) {
        this(baseType, undefined, pointerSize);
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
}
