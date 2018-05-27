package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Operand;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.Entity;

public class Address extends Expression {
    Entity entity;

    public Address(Type type, Entity entity) {
        super(type);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public Operand getAddress () {
        return entity.getAddress();
    }

    @Override
    public MemoryReference getMemoryReference () {
        return entity.getMemoryReference();
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isAddress() {
        return true;
    }

    @Override
    public Entity getEntityForce() {
        return entity;
    }
}
