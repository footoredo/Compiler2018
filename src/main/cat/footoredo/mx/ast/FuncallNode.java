package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

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

    @Override
    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
