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
        /*if (type == null)
            throw new Error ("wtf??ASDASDSAD");*/
        this.entity = entity;
    }

    public String getName () {
        return entity.getName();
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public MemoryReference getMemoryReference () {
        return ((cat.footoredo.mx.entity.Variable)entity).getMemory();
    }

    /*@Override
    public Address getAddressNode (Type type) {
        // System.out.println ("asdasd");
        return new Address(type, entity);
    }*/

    public Register getRegister () {
        return ((cat.footoredo.mx.entity.Variable)entity).getRegister();
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
