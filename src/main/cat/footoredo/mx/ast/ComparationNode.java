package cat.footoredo.mx.ast;

import cat.footoredo.mx.type.BooleanType;
import cat.footoredo.mx.type.Type;

public class ComparationNode extends ArithmeticOpNode {
    public ComparationNode (ExpressionNode lhs, String operator, ExpressionNode rhs) {
        super (lhs, operator, rhs);
    }

    @Override
    public Type getType() {
        return new BooleanType();
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
