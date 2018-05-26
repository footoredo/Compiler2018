package cat.footoredo.mx.asm;

public class DirectMemoryReference extends MemoryReference {
    private Literal value;

    public DirectMemoryReference(Literal value) {
        this.value = value;
    }

    public Literal getValue() {
        return value;
    }

    @Override
    public void fixOffset(long diff) {
        throw new Error("DirectMemoryReference#fixOffset");
    }

    @Override
    protected int cmp(DirectMemoryReference memoryReference) {
        return value.compareTo(memoryReference.value);
    }

    @Override
    protected int cmp(IndirectMemoryReference memoryReference) {
        return 1;
    }

    public String toString () {
        return toSource(SymbolTable.dummy());
    }

    @Override
    public String toSource(SymbolTable table) {
        return value.toSource(table);
    }

    @Override
    public int compareTo(MemoryReference memoryReference) {
        return -(memoryReference.compareTo(this));
    }
}
