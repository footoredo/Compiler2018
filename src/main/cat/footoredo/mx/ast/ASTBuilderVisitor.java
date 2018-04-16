package cat.footoredo.mx.ast;

import cat.footoredo.mx.cst.MxParser;
import cat.footoredo.mx.cst.MxVisitor;
import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.type.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilderVisitor implements MxVisitor <Node> {
    private static Location getLocation (TerminalNode node) {
        return new Location(node.getSymbol());
    }

    private static Location getLocation (ParserRuleContext ctx) {
        return new Location(ctx.start);
    }

    @Override
    public AST visitCompilationUnit(MxParser.CompilationUnitContext ctx) {
        // System.out.println ("123" + ctx.EOF().getSymbol().getTokenSource().getSourceName());
        Declarations declarations = new Declarations();

        for (MxParser.ClassDeclarationContext classDeclarationContext : ctx.classDeclaration()) {
            ClassNode classNode = visitClassDeclaration (classDeclarationContext);
            declarations.addClass(classNode);
        }

        for (MxParser.MethodDeclarationContext methodDeclarationContext : ctx.methodDeclaration()) {
            MethodNode methodNode = visitMethodDeclaration(methodDeclarationContext);
            declarations.addFun (new Function(methodNode));
        }

        for (MxParser.VariableDeclarationContext variableDeclarationContext : ctx.variableDeclaration()) {
            VariableDeclarationNode variableDeclarationNode = visitVariableDeclaration(variableDeclarationContext);
            declarations.addVar(new Variable(variableDeclarationNode));
        }

        return new AST(getLocation(ctx), declarations);
    }

    @Override
    public ExprNode visitExpression(MxParser.ExpressionContext ctx) {
        Location location = getLocation(ctx);
        if (ctx.primary() != null) {                                            // primary
            return visitPrimary(ctx.primary());
        }
        else if (ctx.Identifier() != null) {                                    // expression.Identifier
            ExprNode expr = visitExpression(ctx.expression(0));
            return new MemberNode(expr, ctx.Identifier().getText());
        }
        else if (ctx.creator() != null) {                                       // new creator
            return new NewNode(location, visitCreator(ctx.creator()));
        }
        else if (ctx.getChildCount() == 2) {
            ExprNode exprNode = visitExpression(ctx.expression(0));
            if (ctx.getChild(0) instanceof TerminalNode) {                    // prefix unary expression
                return new UnaryOpNode(location, ctx.getChild(0).getText(), exprNode, true);
            }
            else {                                                              // suffix unary expression
                return new UnaryOpNode(location, ctx.getChild(1).getText(), exprNode, false);
            }
        }
        else if (ctx.getChildCount() == 3) {
            if (ctx.getChild(0) instanceof TerminalNode) {                    // ( expression )
                return visitExpression(ctx.expression(0));
            }
            else {                                                              // binary expression
                ExprNode lhs = visitExpression(ctx.expression(0)),
                        rhs = visitExpression(ctx.expression(1));
                if (ctx.getChild(1).getText().equals("=")) {                  // assign
                    return new AssignNode(lhs, rhs);
                }
                else {                                                          // binary op expression
                    String operator = ctx.getChild(1).getText();
                    if (operator.equals("&&") || operator.equals("||"))
                        return new LogicalOpNode(lhs, operator, rhs);
                    else
                        return new BinaryOpNode(lhs, operator, rhs);
                }
            }
        }
        else {
        }
        return null;
    }

    @Override
    public ExpressionListNode visitExpressionList(MxParser.ExpressionListContext ctx) {
        List<ExprNode> exprs = new ArrayList<ExprNode>();
        for (MxParser.ExpressionContext expressionContext: ctx.expression()){
            exprs.add(visitExpression(expressionContext));
        }
        return new ExpressionListNode(getLocation(ctx), exprs);
    }

    @Override
    public CreatorNode visitCreator(MxParser.CreatorContext ctx) {
        if (ctx.primitiveType() != null) {
            return new CreatorNode(getLocation(ctx), visitPrimitiveType(ctx.primitiveType()).getTypeRef());
        }
        else if (ctx.arrayCreator() != null) {
            return visitArrayCreator(ctx.arrayCreator());
        }
        else {
            UserTypeRef userTypeRef = (UserTypeRef) visitClassType(ctx.classType()).getTypeRef();
            List<ExprNode> args = null;
            if (ctx.arguements() == null) {
                args = new ArrayList<ExprNode>();
            }
            else {
                args = visitArguements(ctx.arguements()).getExprs();
            }
            return new CreatorNode(getLocation(ctx), userTypeRef, args);
        }
    }

    @Override
    public CreatorNode visitArrayCreator(MxParser.ArrayCreatorContext ctx) {
        TypeRef baseTypeRef = visitBaseTypeSpec(ctx.baseTypeSpec()).getTypeRef();
        int specifiedDimensionCount = ctx.expression().size();
        int totalDimensionCount = (ctx.getChildCount() - 1 - specifiedDimensionCount) / 2;
        ArrayTypeRef arrayTypeRef = new ArrayTypeRef(baseTypeRef);
        for (int i = 1; i < totalDimensionCount; ++ i)
            arrayTypeRef = new ArrayTypeRef(arrayTypeRef);
        List<ExprNode> length = new ArrayList<ExprNode>();
        for (MxParser.ExpressionContext expressionContext : ctx.expression()) {
            length.add(visitExpression(expressionContext));
        }
        return new CreatorNode(getLocation(ctx), arrayTypeRef, length);
    }

    @Override
    public ExpressionListNode visitArguements(MxParser.ArguementsContext ctx) {
        if (ctx.expressionList() != null) {
            return visitExpressionList(ctx.expressionList());
        }
        else {
            List<ExprNode> exprs = new ArrayList<ExprNode>();
            return new ExpressionListNode(getLocation(ctx), exprs);
        }
    }

    @Override
    public ExprNode visitPrimary(MxParser.PrimaryContext ctx) {
        if (ctx.Identifier() != null)
            return new VariableNode(getLocation(ctx.Identifier()), ctx.Identifier().getText());
        else
            return visitLiteral(ctx.literal());
    }

    @Override
    public LiteralNode visitLiteral(MxParser.LiteralContext ctx) {
        Location location = getLocation(ctx);
        if (ctx.IntLiteral() != null) {
            int value = Integer.parseInt(ctx.IntLiteral().getText());
            return new IntegerLiteralNode(location, value);
        }
        else if (ctx.StringLiteral() != null) {
            String value = ctx.StringLiteral().getText();
            return new StringLiteralNode(location, value);
        }
        else if (ctx.BoolLiteral() != null) {
            boolean value = (ctx.BoolLiteral().getText() == "true");
            return new BooleanLiteralNode(location, value);
        }
        else {
            return null;
        }
    }

    @Override
    public VariableDeclarationNode visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        TypeNode typeNode = visitTypeSpec(ctx.typeSpec());
        VariableDeclaratorNode variableDeclaratorNode = visitVariableDeclarator(ctx.variableDeclarator());
        return new VariableDeclarationNode(typeNode, variableDeclaratorNode.getName(), variableDeclaratorNode.getInitExpr());
    }

    @Override
    public VariableDeclaratorNode visitVariableDeclarator(MxParser.VariableDeclaratorContext ctx) {
        VariableDeclaratorIdNode variableDeclaratorIdNode = visitVariableDeclaratorId(ctx.variableDeclaratorId());
        ExprNode exprNode = visitVariableInitializer(ctx.variableInitializer());
        return new VariableDeclaratorNode(variableDeclaratorIdNode.getName(), exprNode);
    }

    @Override
    public VariableDeclaratorIdNode visitVariableDeclaratorId(MxParser.VariableDeclaratorIdContext ctx) {
        return new VariableDeclaratorIdNode(ctx.Identifier().getText());
    }

    @Override
    public ExprNode visitVariableInitializer(MxParser.VariableInitializerContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public TypeNode visitPrimitiveType(MxParser.PrimitiveTypeContext ctx) {
        Location location = getLocation(ctx);
        TypeRef typeRef = null;
        switch (ctx.getStart().getText()) {
            case "bool": typeRef = new BooleanTypeRef(location); break;
            case "int": typeRef = new IntegerTypeRef(location); break;
            case "string": typeRef = new StringTypeRef(location); break;
        }
        return new TypeNode(typeRef);
    }

    @Override
    public TypeNode visitTypeSpec(MxParser.TypeSpecContext ctx) {
        if (ctx.baseTypeSpec() != null) {
            return visitBaseTypeSpec(ctx.baseTypeSpec());
        }
        else {
            TypeNode baseType = visitTypeSpec(ctx.typeSpec());
            return new TypeNode(new ArrayTypeRef(baseType.getTypeRef()));
        }
    }

    @Override
    public TypeNode visitBaseTypeSpec(MxParser.BaseTypeSpecContext ctx) {
        if (ctx.primitiveType() != null) {
            return visitPrimitiveType(ctx.primitiveType());
        }
        else {
            return visitClassType(ctx.classType());
        }
    }

    @Override
    public TypeNode visitClassType(MxParser.ClassTypeContext ctx) {
        return new TypeNode(new UserTypeRef(getLocation(ctx.Identifier()), ctx.Identifier().getText()));
    }

    @Override
    public ClassNode visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        return null;
    }

    @Override
    public Node visitClassBody(MxParser.ClassBodyContext ctx) {
        return null;
    }

    @Override
    public Node visitMemberDeclaration(MxParser.MemberDeclarationContext ctx) {
        return null;
    }

    @Override
    public MethodNode visitMethodDeclaration(MxParser.MethodDeclarationContext ctx) {
        TypeNode typeNode = null;
        if (ctx.typeSpec() != null)
            typeNode = visitTypeSpec(ctx.typeSpec());
        else
            typeNode = new TypeNode(new VoidTypeRef(getLocation(ctx)));
        return null;
    }

    @Override
    public Node visitParameters(MxParser.ParametersContext ctx) {
        return null;
    }

    @Override
    public Node visitParametersList(MxParser.ParametersListContext ctx) {
        return null;
    }

    @Override
    public Node visitParameter(MxParser.ParameterContext ctx) {
        return null;
    }

    @Override
    public Node visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        return null;
    }

    @Override
    public Node visitBlock(MxParser.BlockContext ctx) {
        return null;
    }

    @Override
    public Node visitBlockStatement(MxParser.BlockStatementContext ctx) {
        return null;
    }

    @Override
    public Node visitStatement(MxParser.StatementContext ctx) {
        return null;
    }

    @Override
    public Node visitForControl(MxParser.ForControlContext ctx) {
        return null;
    }

    @Override
    public Node visitForInit(MxParser.ForInitContext ctx) {
        return null;
    }

    @Override
    public Node visitForUpdate(MxParser.ForUpdateContext ctx) {
        return null;
    }

    @Override
    public Node visitLocalVariableDeclarationStatement(MxParser.LocalVariableDeclarationStatementContext ctx) {
        return null;
    }

    @Override
    public Node visitLocalVariableDeclaration(MxParser.LocalVariableDeclarationContext ctx) {
        return null;
    }

    @Override
    public Node visit(ParseTree parseTree) {
        parseTree.accept(this);
        return null;
    }

    @Override
    public Node visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override
    public Node visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public Node visitErrorNode(ErrorNode errorNode) {
        return null;
    }
}
