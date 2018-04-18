package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

abstract class UnaryArithmeticNode extends UnaryOpNode {
    UnaryArithmeticNode(Location location, String operator, ExpressionNode expr) {
        super(location, operator, expr);
    }
}
