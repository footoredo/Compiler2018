package cat.footoredo.mx.entity;

import cat.footoredo.mx.asm.ImmediateValue;
import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Symbol;

public class ConstantEntry {
    private String value;
    private Symbol symbol;
    private MemoryReference memoryReference;
    private ImmediateValue address;

    public ConstantEntry(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Symbol getSymbol() {
        if (symbol == null) {
            throw new Error("must not happen: symbol == null");
        }
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public MemoryReference getMemoryReference() {
        return memoryReference;
    }

    public void setMemoryReference(MemoryReference memoryReference) {
        /*if (this.memoryReference == null) {
            throw new Error("must not happen: memoryReference == null");
        }*/
        this.memoryReference = memoryReference;
    }

    public ImmediateValue getAddress() {
        return address;
    }

    public void setAddress(ImmediateValue address) {
        this.address = address;
    }
}
