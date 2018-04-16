package cat.footoredo.mx.ast;

public class VariableDeclaratorNode extends Node {
    private String name;
    private ExprNode initExpr;

    public VariableDeclaratorNode(String name, ExprNode initExpr) {
        super ();
        this.name = name;
        this.initExpr = initExpr;
    }

    public String getName() {
        return name;
    }

    public ExprNode getInitExpr() {
        return initExpr;
    }
}
