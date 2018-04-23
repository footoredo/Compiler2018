package cat.footoredo.mx.ast;

import cat.footoredo.mx.type.BuiltinTypeRef;
import cat.footoredo.mx.type.Type;

public class BuiltinTypeNode extends TypeDefinition {
    public BuiltinTypeNode(BuiltinTypeRef typeRef) {
        super(new TypeNode(typeRef));
    }

    @Override
    public Type definingType() {
        return ((BuiltinTypeRef) getTypeRef()).definingType();
    }

    @Override
    public <T> T accept(TypeDefinitionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
