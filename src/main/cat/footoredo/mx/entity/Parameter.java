package cat.footoredo.mx.entity;

import cat.footoredo.mx.asm.Operand;
import cat.footoredo.mx.ast.ParameterNode;
import cat.footoredo.mx.ast.TypeNode;

public class Parameter extends Variable {
    public Parameter (TypeNode type, String name) {
        super (type, name);
    }
    public Parameter (ParameterNode parameterNode) {
        super (parameterNode.getTypeNode(), parameterNode.getName());
    }

    private Operand parameterSpace;

    public Operand getParameterSpace() {
        return parameterSpace;
    }

    public void setParameterSpace(Operand parameterSpace) {
        this.parameterSpace = parameterSpace;
    }
}
