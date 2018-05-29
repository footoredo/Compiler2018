package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.*;
import cat.footoredo.mx.entity.ConstantEntry;
import cat.footoredo.mx.utils.StringUtils;

public class String extends Expression {
    private ConstantEntry entry;

    public String(Type type, ConstantEntry entry) {
        super(type);
        this.entry = entry;
    }

    public ConstantEntry getEntry() {
        return entry;
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    public Symbol getSymbol () {
        return entry.getSymbol();
    }

    public ImmediateValue getAsmValue() {
        return entry.getAddress();
    }

    @Override
    public Operand getAddress() {
        return entry.getAddress();
    }

    @Override
    public MemoryReference getMemoryReference() {
        return entry.getMemoryReference();
    }

    public java.lang.String getValue() {
        return entry.getValue();
    }
}
