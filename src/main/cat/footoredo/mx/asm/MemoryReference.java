package cat.footoredo.mx.asm;

abstract public class MemoryReference extends Operand implements Comparable<MemoryReference> {
    @Override
    public boolean isMemoryReference() {
        return true;
    }

    abstract public void fixOffset (long diff);
    abstract protected int cmp (DirectMemoryReference memoryReference);
    abstract protected int cmp (IndirectMemoryReference memoryReference);
}
