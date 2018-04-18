package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

abstract public class LHSNode extends ExpressionNode {
    abstract public Location getLocation();
}
