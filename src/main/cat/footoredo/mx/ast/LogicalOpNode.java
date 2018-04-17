package cat.footoredo.mx.ast;

public class LogicalOpNode extends BinaryOpNode {
    public LogicalOpNode (ExpressionNode lhs, String operator, ExpressionNode rhs) {
        super (lhs, operator, rhs);
    }
}
