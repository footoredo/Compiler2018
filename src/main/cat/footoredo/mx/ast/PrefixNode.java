package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

public class PrefixNode extends UnaryArithmeticNode {
    public PrefixNode(Location location, String operator, ExpressionNode expr) {
        super(location, operator, expr);
    }
    @Override
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

}
