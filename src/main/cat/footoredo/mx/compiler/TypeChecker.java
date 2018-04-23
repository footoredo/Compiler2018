package cat.footoredo.mx.compiler;

import cat.footoredo.mx.OJ.Semantic;
import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.DefinedFunction;
import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.Parameter;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.Type;
import cat.footoredo.mx.type.TypeTable;

import java.util.List;

public class TypeChecker extends Visitor {
    private TypeTable typeTable;
    private Function currentFunction;

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

    private void checkVariable(Variable variable) {
        if (!isValidVariableType(variable.getType()))
            throw new SemanticException(variable.getLocation(), "variable " + variable.getName() + "has invalid variable type");
        if (variable.hasInitializer()) {
            visitExpression(variable.getInitializer());
            if (!areSameType(variable.getType(), variable.getInitializer().getType()))
                throw new SemanticException(variable.getInitializer().getLocation(), "initializer has incompatible type");
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
        super.visit(node);
        if (node.hasJudge())
            checkCond(node.getJudge());
        return null;
    }

    public Void visit(WhileNode node) {
        super.visit(node);
        checkCond(node.getJudge());
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
        checkLhs(node.getLhs());
        checkRhs(node.getRhs());
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
        if (node.getOperator() == "+") {
            if (!areSameType(node.getLhs().getType(), node.getRhs().getType()))
                throw new SemanticException(node.getLhs().getLocation(), "incompatible operands");
            if (!node.getLhs().getType().isInteger() && !node.getLhs().getType().isString())
                throw new SemanticException(node.getLhs().getLocation(), "wrong operand type for \"+\"");
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
}
