package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Location;

import java.util.List;

public class ConstructorNode extends MethodNode {
    public ConstructorNode(Location location, String name, List<ParameterNode> parameterNodes, BlockNode block) {
        super (new MethodDescriptionNode(location, name, parameterNodes), block);
    }
}
