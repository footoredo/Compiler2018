package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Operand;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.Entity;

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

    public Operand getAddress () {
        return entity.getAddress();
    }

    public MemoryReference getMemoryReference () {
        return entity.getMemoryReference();
    }

    public Address addressNode (Type type) {
        return new Address(type, entity);
    }
}
