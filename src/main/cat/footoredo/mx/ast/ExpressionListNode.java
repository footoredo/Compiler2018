package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

import java.util.ArrayList;
import java.util.List;

public class ExpressionListNode extends Node {
    private List<ExprNode> exprs;

    public ExpressionListNode(List<ExprNode> exprs) {
        super ();
        this.exprs = exprs;
    }

    public ExpressionListNode() {
        super ();
        this.exprs = new ArrayList<ExprNode>();
    }

    public void addExpression (ExprNode expr) {
        exprs.add(expr);
    }

    public List<ExprNode> getExprs() {
        return exprs;
    }
}
