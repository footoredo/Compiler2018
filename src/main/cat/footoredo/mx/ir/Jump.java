package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Location;

public class Jump extends Statement {
    private Label target;

    public Jump(Location location, Label target) {
        super(location);
        this.target = target;
    }

    public Label getTarget() {
        return target;
    }

    @Override
    public <S, E> S accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
