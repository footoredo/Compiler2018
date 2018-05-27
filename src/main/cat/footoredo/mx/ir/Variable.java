package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Operand;
import cat.footoredo.mx.asm.Register;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.Entity;

import java.lang.String;

public class Variable extends Expression {
    private Entity entity;

    public Variable(Type type, Entity entity) {
        super(type);
        this.entity = entity;
    }

    public String getName () {
        return entity.getName();
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
    public Address getAddressNode (Type type) {
        return new Address(type, entity);
    }

    public Register getRegister () {
        return entity.getRegister();
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isVariable() {
        return true;
    }

    @Override
    public Entity getEntityForce() {
        return entity;
    }
}
