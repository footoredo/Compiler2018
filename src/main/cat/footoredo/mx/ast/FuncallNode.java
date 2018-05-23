package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.type.FunctionType;
import cat.footoredo.mx.type.Type;

import java.util.List;

public class FuncallNode extends ExpressionNode {
    private ExpressionNode caller;
    private List<ExpressionNode> params;

    public FuncallNode(ExpressionNode caller, List<ExpressionNode> params) {
        super ();
        this.caller = caller;
        this.params = params;
    }

    @Override
    public Location getLocation() {
        return caller.getLocation();
    }

    public ExpressionNode getCaller() {
        return caller;
    }

    public List<ExpressionNode> getParams() {
        return params;
    }

    public int getArgc() {
        return params.size();
    }

    public FunctionType getFunctionType() {
        return (FunctionType) caller.getType();
    }

    public Type getReturnType () {
        return getFunctionType().getReturnType();
    }

    @Override
    public Type getType() {
        /*if (caller.getType() == null)
            System.out.println(caller.getLocation().toString());*/
        // System.out.println("asdasd" + (caller.getType() == null));
        try {
            // System.out.println("asdasdasdasdasdasdasdad");
            return getReturnType();
        } catch (NullPointerException e) {
            // System.out.println("fucking fuck" + ((MemberNode)caller).getName());
            throw e;
        }
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
