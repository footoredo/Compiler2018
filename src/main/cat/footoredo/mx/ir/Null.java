package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.Location;

public class Null extends Expression {
    public Null(Type type) {
        super(type);
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
