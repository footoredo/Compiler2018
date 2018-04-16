package cat.footoredo.mx.ast;

import java.util.ArrayList;
import java.util.List;

public class ParameterListNode extends Node {
    private List<ParameterNode> parameterNodes;

    public ParameterListNode () {
        this.parameterNodes = new ArrayList<ParameterNode>();
    }

    public void addParameterNode (ParameterNode parameterNode) {
        parameterNodes.add (parameterNode);
    }

    public List<ParameterNode> getParameterNodes() {
        return parameterNodes;
    }
}
