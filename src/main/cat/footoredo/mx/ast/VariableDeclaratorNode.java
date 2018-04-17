package cat.footoredo.mx.ast;

public class VariableDeclaratorNode extends Node {
    private String name;
    private ExpressionNode initExpr;

    public VariableDeclaratorNode(String name, ExpressionNode initExpr) {
        super ();
        this.name = name;
        this.initExpr = initExpr;
        // System.out.println("ass");
    }

    public String getName() {
        return name;
    }

    public ExpressionNode getInitExpr() {
        return initExpr;
    }
}
