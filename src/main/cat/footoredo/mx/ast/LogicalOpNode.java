package cat.footoredo.mx.ast;

public class LogicalOpNode extends BinaryOpNode {
    public LogicalOpNode (ExprNode lhs, String operator, ExprNode rhs) {
        super (lhs, operator, rhs);
    }
}
