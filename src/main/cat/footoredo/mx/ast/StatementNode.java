package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

abstract public class StatementNode extends BlockStatementNode {
    abstract public Location getLocation();
}
