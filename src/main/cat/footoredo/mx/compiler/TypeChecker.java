package cat.footoredo.mx.compiler;

import cat.footoredo.mx.OJ.Semantic;
import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.ClassType;
import cat.footoredo.mx.type.MemberType;
import cat.footoredo.mx.type.Type;
import cat.footoredo.mx.type.TypeTable;

import java.util.List;

public class TypeChecker extends Visitor {
    private TypeTable typeTable;
    private Function currentFunction;
    private boolean inLoopBody;

    public TypeChecker(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    public void check(AST ast) throws SemanticException {
        for (Variable variable: ast.getVariables()) {
            checkVariable(variable);
        }
        for (DefinedFunction function: ast.getFunctions()) {
            checkFunction(function);
        }
        for (ClassNode cls: ast.getClasses()) {
            checkClass(cls);
        }
    }
    
    private void checkAssignType(Location location, Type a, Type b) {
        if (a instanceof ClassType) {
            if (!b.isNull() && !areSameType(a, b))
                throw new SemanticException(location, "incompatible assign type");
        }
        else {
            if (!areSameType(a, b))
                throw new SemanticException(location, "incompatible assign type");
        }
    }

    @Override
    public Void visit(LocalVariableDeclarationNode node) {
        super.visit(node);
        if (node.hasInitExpr()) {
            System.out.println(node.getInitExpr().getType().isNull());
            checkAssignType(node.getLocation(), node.getTypeNode().getType(), node.getInitExpr().getType());
        }
        return null;
    }

    private void checkVariable(Variable variable) {
        // System.out.println("Asd");
        if (!isValidVariableType(variable.getType()))
            throw new SemanticException(variable.getLocation(), "variable " + variable.getName() + "has invalid variable type");
        if (variable.hasInitializer()) {
            visitExpression(variable.getInitializer());
            checkAssignType(variable.getLocation(), variable.getType(), variable.getInitializer().getType());
        }
    }

    private void checkFunction(Function function) {
        for (Parameter parameter: function.getParameters()) {
            checkVariable(parameter);
        }
        if (function instanceof DefinedFunction) {
            currentFunction = function;
            visitStatement(((DefinedFunction) function).getBlock());
        }
    }

    private void checkClass(ClassNode cls) {
        for (Variable variable: cls.getMemberVariables()) {
            checkVariable(variable);
        }
        for (Function function: cls.getMemberMethods()) {
            checkFunction(function);
        }
    }

    private boolean isValidVariableType(Type type) {
        return !(type.isVoid());
    }

    public Void visit(IfNode node) {
        super.visit(node);
        checkCond(node.getJudge());
        return null;
    }

    public Void visit(ForNode node) {
        boolean originalInLoopBody = inLoopBody;
        inLoopBody = true;
        super.visit(node);
        inLoopBody = originalInLoopBody;
        if (node.hasJudge())
            checkCond(node.getJudge());
        return null;
    }

    public Void visit(WhileNode node) {
        boolean originalInLoopBody = inLoopBody;
        inLoopBody = true;
        super.visit(node);
        inLoopBody = originalInLoopBody;
        checkCond(node.getJudge());
        return null;
    }

    public Void visit(ContinueNode node) {
        if (!inLoopBody)
            throw new SemanticException(node.getLocation(), "continue in non-loop block");
        return null;
    }

    public Void visit(BreakNode node) {
        if (!inLoopBody)
            throw new SemanticException(node.getLocation(), "break in non-loop block");
        return null;
    }

    public Void visit(ReturnNode node) {
        super.visit(node);
        if (currentFunction.getReturnType().isVoid()) {
            if (node.hasExpr())
                throw new SemanticException(node.getLocation(), "return in void function");
        }
        else {
            if (!areSameType(node.getExpr().getType(), currentFunction.getReturnType()))
                throw new SemanticException(node.getLocation(), "incompatible return type");
        }
        return null;
    }

    public Void visit(AssignNode node) {
        super.visit(node);
        if (!(node.getLhs() instanceof ArefNode ||
            node.getLhs() instanceof VariableNode ||
            node.getLhs() instanceof MemberNode))
            throw new SemanticException(node.getLocation(), "not valid lhs node");
        checkLhs(node.getLhs());
        checkRhs(node.getRhs());
        // System.out.println("asda");
        checkAssignType(node.getLocation(), node.getLhs().getType(), node.getRhs().getType());
        return null;
    }

    private void checkCond(ExpressionNode cond) {
        // System.out.println(cond.getType());
        if (!(cond.getType().isBoolean()))
            throw new SemanticException(cond.getLocation(), "not valid condition expression");
    }

    private void checkLhs(ExpressionNode lhs) {
        if (lhs.getType().isVoid())
            throw new SemanticException(lhs.getLocation(), "LHS is void");
    }

    private void checkRhs(ExpressionNode rhs) {
        if (rhs.getType().isVoid())
            throw new SemanticException(rhs.getLocation(), "RHS is void");
    }

    private boolean areSameType(Type a, Type b) {
        return a.hashCode() == b.hashCode();
    }

    public Void visit(ArithmeticOpNode node) {
        super.visit(node);
        // System.out.println("sadas");
        if (node.getOperator().equals("+")) {
            // System.out.println("asdasd");
            if (!areSameType(node.getLhs().getType(), node.getRhs().getType()))
                throw new SemanticException(node.getLhs().getLocation(), "incompatible operands");
            if (!node.getLhs().getType().isInteger() && !node.getLhs().getType().isString()) {
                throw new SemanticException(node.getLhs().getLocation(), "wrong operand type for \"+\"");
            }
        }
        else {
            if (!node.getLhs().getType().isInteger())
                throw new SemanticException(node.getLhs().getLocation(), "lhs of arithmetic op is not integer");
            if (!node.getRhs().getType().isInteger())
                throw new SemanticException(node.getRhs().getLocation(), "rhs of arithmetic op is not integer");
        }
        return null;
    }

    public Void visit(LogicalOpNode node) {
        super.visit(node);
        if (!node.getLhs().getType().isBoolean())
            throw new SemanticException(node.getLhs().getLocation(), "lhs of logical op is not boolean");
        if (!node.getRhs().getType().isBoolean())
            throw new SemanticException(node.getRhs().getLocation(), "rhs of logical op is not boolean");
        return null;
    }

    public Void visit(UnaryOpNode node) {
        super.visit(node);
        if (node.getOperator().equals("!")) {
            if (!node.getExpr().getType().isBoolean())
                throw new SemanticException(node.getExpr().getLocation(), "object of unary operator \"!\" is not boolean");
        }
        else {
            if (!node.getExpr().getType().isInteger())
                throw new SemanticException(node.getExpr().getLocation(), "object of unary operator \"" + node.getOperator() + "\" is not integer");
        }
        return null;
    }

    public Void visit(FuncallNode node) {
        super.visit(node);
        if (node.getArgc() != node.getFunctionType().getArgc())
            throw new SemanticException(node.getLocation(), "incompatible argc");
        List<ExpressionNode> funcallParams = node.getParams();
        List<Type> params = node.getFunctionType().getParams().getParamDescriptors();
        int argc = funcallParams.size();
        for (int i = 0; i < argc; ++ i)
            if (!areSameType(funcallParams.get(i).getType(), params.get(i)))
                throw new SemanticException(funcallParams.get(i).getLocation(), "incompatible arg type");
        return null;
    }

    public Void visit(ArefNode node) {
        super.visit(node);
        if (!node.getExpr().getType().isArray())
            throw new SemanticException(node.getLocation(), "accessing a non-array variable by index");
        return null;
    }
}
