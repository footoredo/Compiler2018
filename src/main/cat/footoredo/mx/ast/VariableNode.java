package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Variable;

public class VariableNode extends LHSNode {
    String name;

    public VariableNode (String name) {
        super ();
        this.name = name;
        System.out.println ("Got variable " + this.name);
    }

    public String name () {
        return name;
    }
}
