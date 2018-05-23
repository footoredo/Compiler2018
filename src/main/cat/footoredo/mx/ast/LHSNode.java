package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.Type;

abstract public class LHSNode extends ExpressionNode {
    abstract public Location getLocation();

    @Override
    public boolean isLoadable() {
        Type type = getType();
        return !type.isArray() && !type.isFunction() && !type.isString() && !type.isClass();
    }
}
