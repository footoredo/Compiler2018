package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;

import java.util.List;

public class Call extends Expression {
    private Expression expression;
    private List<Expression> args;

    public Call(Type type, Expression expression, List<Expression> args) {
        super(type);
        this.expression = expression;
        this.args = args;
    }

    public Expression getExpression() {
        return expression;
    }

    public List<Expression> getArgs() {
        return args;
    }
}
