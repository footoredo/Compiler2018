package cat.footoredo.mx.ir;

import cat.footoredo.mx.entity.Location;

public class Assign extends Statement {
    private Expression lhs, rhs;

    public Assign(Location location, Expression lhs, Expression rhs) {
        super(location);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Expression getLhs() {
        return lhs;
    }

    public Expression getRhs() {
        return rhs;
    }
}
