package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.Location;

public class Null extends Integer {
    public Null(Type type) {
        super(type, 0);
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
