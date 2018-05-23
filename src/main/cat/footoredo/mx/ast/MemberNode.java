package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.type.MemberType;
import cat.footoredo.mx.type.Type;

public class MemberNode extends LHSNode {
    protected ExpressionNode expression;
    protected String name;

    public MemberNode (ExpressionNode expression, String name) {
        super ();
        this.expression = expression;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public Location getLocation () {
        return expression.getLocation ();
    }

    public int getOffset () {
        return getSlot().getOffset();
    }

    private Slot getSlot () {
        return ((MemberType)expression.getType()).getMember(name);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        // System.out.println(expression);
        // System.out.println(expression.getType().toString() +  "." + name);
        return ((MemberType)expression.getType()).getMemberType(name);
    }

}
