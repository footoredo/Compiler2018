package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.ClassType;
import cat.footoredo.mx.type.MemberType;
import cat.footoredo.mx.type.Type;
import cat.footoredo.mx.type.TypeTable;

import java.util.List;

public class TypeResolver extends Visitor
        implements TypeDefinitionVisitor<Void>, EntityVisitor<Void> {
    private TypeTable typeTable;

    public TypeResolver(TypeTable types) {
        this.typeTable = types;
    }

    public void resolve(AST ast) {
        defineTypes(ast.getTypeDefinitions());
        for (TypeDefinition t: ast.getTypeDefinitions()) {
            t.accept(this);
        }
        for (Entity e: ast.getDeclarations()) {
            e.accept(this);
        }
    }

    private void defineTypes(List<TypeDefinition> typeDefinitions) {
        for (TypeDefinition t: typeDefinitions) {
            if (typeTable.isDefined(t.getTypeRef())) {
                throw new SemanticException("duplicated type definition of " + t.getTypeRef().toString());
            }
            else {
                typeTable.put(t.getTypeRef(), t.definingType());
            }
        }
    }

    private void bindType(TypeNode n) {
        if (!n.isResolved()) {
            n.setType(typeTable.get(n.getTypeRef()));
        }
    }

    public Void visit(TypeDefinition typeDefinition) {
        resolveTypeDefinition(typeDefinition);
        return null;
    }

    /* public Void visit(BuiltinTypeNode builtinTypeNode) {
        return null;
    }*/

    @Override
    public Void visit(DefinedFunction definedFunction) {
        bindType(definedFunction.getTypeNode());
        bindType(definedFunction.getReturnTypeNode());
        resolveParameters(definedFunction.getParameters());
        // System.out.println("resolving defined function " + definedFunction.getName());
        visitStatement(definedFunction.getBlock());
        return null;
    }

    @Override
    public Void visit(BuiltinFunction builtinFunction) {
        bindType(builtinFunction.getTypeNode());
        bindType(builtinFunction.getReturnTypeNode());
        resolveParameters(builtinFunction.getParameters());
        return null;
    }

    @Override
    public Void visit(Variable variable) {
        bindType(variable.getTypeNode());
        if (variable.hasInitializer()) visitExpression(variable.getInitializer());
        return null;
    }

    @Override
    public Void visit(VariableNode node) {
        // System.out.println("type resolving variable " + node.getName() + "@ " + node.getLocation());
        super.visit(node);
        bindType(node.getEntity().getTypeNode());
        // System.out.println(node.getEntity().getType());
        return null;
    }

    /*private void resolveClassNode(ClassNode classNode) {
        ClassType classType = (ClassType) typeTable.get(classNode.getTypeRef());
        if (classType == null)
            throw new Error(classNode.getLocation().toString() + ": wtf??");
        for (Slot s: classType.getMembers()) {
            bindType(s.getTypeNode());
        }
        for (Entity e: classNode.getEntitis()) {
            e.accept(this);
        }
    }*/

    private void resolveTypeDefinition(TypeDefinition typeDefinition) {
        Type type = typeTable.get(typeDefinition.getTypeRef());
        if (!(type instanceof MemberType)) return;
        MemberType memberType = (MemberType) type;
        /*if (classType == null)
            throw new Error(classNode.getLocation().toString() + ": wtf??");*/
        for (Slot s: memberType.getMembers()) {
            bindType(s.getTypeNode());
        }
        if (typeDefinition instanceof ClassNode) {
            for (Entity e : ((ClassNode)typeDefinition).getEntitis()) {
                e.accept(this);
            }
        }
    }

    private void resolveParameters(List<Parameter> parameters) {
        for (Parameter parameter: parameters) {
            bindType(parameter.getTypeNode());
        }
    }

    @Override
    public Void visit(IntegerLiteralNode node) {
        bindType(node.getTypeNode());
        return super.visit(node);
    }

    @Override
    public Void visit(StringLiteralNode node) {
        bindType(node.getTypeNode());
        return super.visit(node);
    }

    @Override
    public Void visit(BooleanLiteralNode node) {
        bindType(node.getTypeNode());
        return super.visit(node);
    }

    @Override
    public Void visit(NullLiteralNode node) {
        bindType(node.getTypeNode());
        return super.visit(node);
    }

    @Override
    public Void visit(NewNode node) {
        bindType(node.getTypeNode());
        return super.visit(node);
    }

    @Override
    public Void visit(LocalVariableDeclarationNode node) {
        bindType(node.getTypeNode());
        return super.visit(node);
    }
}
