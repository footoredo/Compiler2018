package cat.footoredo.mx.ast;

import java.util.ArrayList;
import java.util.List;

public class ExpressionListNode extends Node {
    private List<ExpressionNode> exprs;

    public ExpressionListNode(List<ExpressionNode> exprs) {
        super ();
        this.exprs = exprs;
    }

    public ExpressionListNode() {
        super ();
        this.exprs = new ArrayList<ExpressionNode>();
    }

    public void addExpression (ExpressionNode expr) {
        exprs.add(expr);
    }

    public List<ExpressionNode> getExprs() {
        return exprs;
    }
}
