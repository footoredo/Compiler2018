package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.ImmediateValue;
import cat.footoredo.mx.asm.IntegerLiteral;
import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Type;

public class Integer extends Expression {
    private long value;

    public Integer(Type type, long value) {
        super(type);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public ImmediateValue getAsmValue () {
        return new ImmediateValue(new IntegerLiteral(value));
    }

    @Override
    public MemoryReference getMemoryReference() {
        throw new Error("must not happen: IntValue#memref");
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
