package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Entity;
import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.ir.Expression;
import cat.footoredo.mx.type.MemberType;
import cat.footoredo.mx.type.Type;

public class MemberNode extends LHSNode {
    protected ExpressionNode expression;
    protected String name;
    private Expression instance;
    private Entity entity;

    public MemberNode (ExpressionNode expression, String name) {
        super ();
        this.expression = expression;
        this.name = name;
    }

    public Expression getInstance() {
        return instance;
    }

    public void setInstance(Expression instance) {
        this.instance = instance;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public String getTypeName () {
        return expression.getMemberType().getName();
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
        return (expression.getMemberType()).getMember(name);
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type getType() {
        // System.out.println(expression);
        // System.out.println(expression.getType().toString() +  "." + name);
        return (expression.getMemberType()).getMemberType(name);
    }

}
