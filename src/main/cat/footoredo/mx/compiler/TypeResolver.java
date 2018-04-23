package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.ClassType;
import cat.footoredo.mx.type.TypeTable;

import java.util.List;

public class TypeResolver extends Visitor
        implements TypeDefinitionVisitor<Void>, EntityVisitor<Void> {
    private TypeTable typeTable;

    public TypeResolver() {
        this.typeTable = new TypeTable();
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

    @Override
    public Void visit(ClassNode classNode) {
        resolveClassNode(classNode);
        return null;
    }

    @Override
    public Void visit(BuiltinTypeNode builtinTypeNode) {
        return null;
    }

    @Override
    public Void visit(DefinedFunction definedFunction) {
        bindType(definedFunction.getReturnType());
        resolveParameters(definedFunction.getParameters());
        visitStatement(definedFunction.getBlock());
        return null;
    }

    @Override
    public Void visit(BuiltinFunction builtinFunction) {
        bindType(builtinFunction.getReturnType());
        resolveParameters(builtinFunction.getParameters());
        return null;
    }

    @Override
    public Void visit(Variable variable) {
        bindType(variable.getTypeNode());
        if (variable.hasInitializer()) visitExpression(variable.getInitializer());
        return null;
    }

    private void resolveClassNode(ClassNode classNode) {
        ClassType classType = (ClassType) typeTable.get(classNode.getTypeRef());
        /*if (classType == null)
            throw new Error(classNode.getLocation().toString() + ": wtf??");*/
        for (Slot s: classType.getMembers()) {
            bindType(s.getTypeNode());
        }
        for (Entity e: classNode.getEntitis()) {
            e.accept(this);
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
        return null;
    }

    @Override
    public Void visit(StringLiteralNode node) {
        bindType(node.getTypeNode());
        return null;
    }

    @Override
    public Void visit(BooleanLiteralNode node) {
        bindType(node.getTypeNode());
        return null;
    }

    @Override
    public Void visit(NewNode node) {
        bindType(node.getType());
        return null;
    }
}
