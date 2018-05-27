package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.Entity;
import cat.footoredo.mx.entity.Function;

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

    public long getArgc () {
        return args.size();
    }

    public Expression getArg (int i) {
        return args.get(i);
    }

    public Function getFunction () {
        Entity entity = expression.getEntityForce();
        if (entity == null || !(entity instanceof Function)) {
            throw new Error("not a funcall");
        }
        return (Function) entity;
    }

    @Override
    public <S, E> E accept(IRVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
