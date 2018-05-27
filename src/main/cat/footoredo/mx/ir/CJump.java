package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Location;

public class CJump extends Statement {
    private Expression cond;
    private Label thenLabel;
    private Label elseLabel;

    public CJump(Location location, Expression cond, Label thenLabel, Label elseLabel) {
        super(location);
        this.cond = cond;
        this.thenLabel = thenLabel;
        this.elseLabel = elseLabel;
    }

    public Expression getCond() {
        return cond;
    }

    public Label getThenLabel() {
        return thenLabel;
    }

    public Label getElseLabel() {
        return elseLabel;
    }

    @Override
    public <S, E> S accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
