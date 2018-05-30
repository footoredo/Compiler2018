package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Location;

public class Return extends Statement {
    Expression expression;
    Label functionEndLabel;

    public Return(Location location, Expression expressionm, Label functionEndLabel) {
        super(location);
        this.expression = expression;
        this.functionEndLabel = functionEndLabel;
    }

    public Label getFunctionEndLabel() {
        return functionEndLabel;
    }

    public boolean hasExpression () {
        return expression != null;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <S, E> S accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
