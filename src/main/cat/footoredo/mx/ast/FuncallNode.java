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
        try {
            System.out.println(((MemberNode) caller).getName());
        }
        catch (Exception e) {

        }
        System.out.println("ASD" + caller.getLocation());
        return (FunctionType) caller.getType();
    }

    @Override
    public Type getType() {
        return ((FunctionType) caller.getType()).getReturnType();
    }

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
