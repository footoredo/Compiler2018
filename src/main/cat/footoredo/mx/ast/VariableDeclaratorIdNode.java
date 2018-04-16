package cat.footoredo.mx.ast;

public class VariableDeclaratorIdNode extends Node {
    private String name;

    public VariableDeclaratorIdNode (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
