package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.MemberType;
import cat.footoredo.mx.type.Type;

public class MemberNode extends LHSNode {
    protected ExpressionNode expr;
    protected String name;

    public MemberNode (ExpressionNode expr, String name) {
        super ();
        this.expr = expr;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getExpr() {
        return expr;
    }

    public Location getLocation () {
        return expr.getLocation ();
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        // System.out.println(expr);
        // System.out.println(expr.getType().toString());
        return ((MemberType)expr.getType()).getMemberType(name);
    }
}
