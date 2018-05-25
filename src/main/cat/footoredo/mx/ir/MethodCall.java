package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Type;

import java.util.List;

public class MethodCall extends Call {
    private Expression thisInstance;

    public MethodCall(Type type, Expression expression, List<Expression> args, Expression thisInstance) {
        super(type, expression, args);
        this.thisInstance = thisInstance;
    }
}
