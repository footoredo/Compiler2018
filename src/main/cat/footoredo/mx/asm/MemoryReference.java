package cat.footoredo.mx.asm;

abstract public class MemoryReference extends Operand implements Comparable<MemoryReference> {
    @Override
    public boolean isMemoryReference() {
        return true;
    }

    private Type type;
    MemoryReference (Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    String getTypeString () {
        switch (type) {
            case INT8: return "BYTE";
            case INT16: return "WORD";
            case INT32: return "DWORD";
            case INT64: return "QWORD";
            default:
                throw new Error("wtf");
        }
    }

    abstract public void fixOffset (long diff);
    abstract protected int cmp (DirectMemoryReference memoryReference);
    abstract protected int cmp (IndirectMemoryReference memoryReference);
}
