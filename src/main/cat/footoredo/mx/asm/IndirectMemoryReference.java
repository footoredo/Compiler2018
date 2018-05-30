package cat.footoredo.mx.asm;

public class IndirectMemoryReference extends MemoryReference {
    Literal offset;
    Register base;
    boolean fixed;

    public IndirectMemoryReference(Type type, Literal offset, Register base, boolean fixed) {
        super (type);
        this.offset = offset;
        this.base = base;
        this.fixed = fixed;
    }

    public IndirectMemoryReference (Type type, long offset, Register base) {
        this (type, new IntegerLiteral(offset), base, true);
    }

    public IndirectMemoryReference (Type type, Symbol offset, Register base) {
        this (type, offset, base, true);
    }

    static public IndirectMemoryReference relocatable(Type type, long offset, Register base) {
        return new IndirectMemoryReference(type, new IntegerLiteral(offset), base, false);
    }

    public Literal getOffset() {
        return offset;
    }

    public Register getBase() {
        return base;
    }

    public boolean isFixed() {
        return fixed;
    }

    public String toString() {
        return toSource(SymbolTable.dummy());
    }

    @Override
    public void fixOffset(long diff) {
        if (fixed) {
            throw new Error("must not happen: fixed = true");
        }
        long currentOffset = ((IntegerLiteral) offset).getValue();
        this.offset = new IntegerLiteral(currentOffset + diff);
        this.fixed = true;
    }

    @Override
    protected int cmp(DirectMemoryReference memoryReference) {
        return -1;
    }

    @Override
    protected int cmp(IndirectMemoryReference memoryReference) {
        return offset.compareTo(memoryReference.offset);
    }

    @Override
    public String toSource(SymbolTable table) {
        if (! fixed) {
            throw new Error("must not happen: writing unfixed variable");
        }
        return getTypeString() + " [" + base.toSource(table) + (offset.isZero() ? "" : " + " + offset.toSource(table)) + "]";
    }

    @Override
    public int compareTo(MemoryReference memoryReference) {
        return 0;
    }

    @Override
    public void collectStatistics(Statistics statistics) {
        base.collectStatistics(statistics);
    }
}
