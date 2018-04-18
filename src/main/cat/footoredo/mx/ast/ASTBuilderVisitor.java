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
    public ExpressionNode visitExpression(MxParser.ExpressionContext ctx) {
        Location location = getLocation(ctx);
        if (ctx.primary() != null) {                                            // primary
            return visitPrimary(ctx.primary());
        }
        else if (ctx.Identifier() != null) {                                    // expression.Identifier
            ExpressionNode expr = visitExpression(ctx.expression(0));
            return new MemberNode(expr, ctx.Identifier().getText());
        }
        else if (ctx.creator() != null) {                                       // new creator
            return new NewNode(location, visitCreator(ctx.creator()));
        }
        else if (ctx.expressionList() != null) {                                // expression (...)
            ExpressionNode caller = visitExpression(ctx.expression(0));
            ExpressionListNode expressionListNode = visitExpressionList(ctx.expressionList());
            return new FuncallNode(caller, expressionListNode.getExprs());
        }
        else if (ctx.getChildCount() == 2) {
            ExpressionNode expressionNode = visitExpression(ctx.expression(0));
            if (ctx.getChild(0) instanceof TerminalNode) {                    // prefix unary expression
                String operand = ctx.getChild(0).getText();
                if (operand.equals("++") || operand.equals("--"))
                    return new PrefixNode(location, operand, expressionNode);
                else
                    return new UnaryOpNode(location, operand, expressionNode);
            }
            else {                                                              // suffix unary expression
                return new SuffixNode(location, ctx.getChild(1).getText(), expressionNode);
            }
        }
        else if (ctx.getChildCount() == 3) {
            if (ctx.getChild(0) instanceof TerminalNode) {                    // ( expression )
                return visitExpression(ctx.expression(0));
            }
            else {                                                              // binary expression
                ExpressionNode lhs = visitExpression(ctx.expression(0)),
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
        else if (ctx.getChildCount() == 4) {                                    // expression [index]
            ExpressionNode expr = visitExpression(ctx.expression(0));
            ExpressionNode index = visitExpression(ctx.expression(1));
            return new ArefNode(expr, index);
        }
        else {
            return null;
        }
    }

    @Override
    public ExpressionListNode visitExpressionList(MxParser.ExpressionListContext ctx) {
        ExpressionListNode expressionListNode = new ExpressionListNode();
        for (MxParser.ExpressionContext expressionContext: ctx.expression()){
            expressionListNode.addExpression(visitExpression(expressionContext));
        }
        return expressionListNode;
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
            List<ExpressionNode> args;
            if (ctx.arguments() == null) {
                args = new ArrayList<>();
            }
            else {
                args = visitArguments(ctx.arguments()).getExprs();
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
        List<ExpressionNode> length = new ArrayList<>();
        for (MxParser.ExpressionContext expressionContext : ctx.expression()) {
            length.add(visitExpression(expressionContext));
        }
        return new CreatorNode(getLocation(ctx), arrayTypeRef, length);
    }

    @Override
    public ExpressionListNode visitArguments(MxParser.ArgumentsContext ctx) {
        if (ctx.expressionList() != null) {
            return visitExpressionList(ctx.expressionList());
        }
        else {
            return new ExpressionListNode();
        }
    }

    @Override
    public ExpressionNode visitPrimary(MxParser.PrimaryContext ctx) {
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
            boolean value = (ctx.BoolLiteral().getText().equals("true"));
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
        // if (ctx.variableInitializer() == null) throw new SemanticError(getLocation(ctx), "sss");
        // System.out.println("asdasd");
        ExpressionNode expressionNode =
                ctx.variableInitializer() == null ? null : visitVariableInitializer(ctx.variableInitializer());
        return new VariableDeclaratorNode(variableDeclaratorIdNode.getName(), expressionNode);
    }

    @Override
    public VariableDeclaratorIdNode visitVariableDeclaratorId(MxParser.VariableDeclaratorIdContext ctx) {
        return new VariableDeclaratorIdNode(ctx.Identifier().getText());
    }

    @Override
    public ExpressionNode visitVariableInitializer(MxParser.VariableInitializerContext ctx) {
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
        Location location = getLocation(ctx);
        String name = ctx.Identifier().getText();
        ClassBodyNode body = visitClassBody(ctx.classBody());
        return new ClassNode(location, name, body);
    }

    @Override
    public ClassBodyNode visitClassBody(MxParser.ClassBodyContext ctx) {
        ClassBodyNode classBodyNode = new ClassBodyNode();
        for (MxParser.MemberDeclarationContext memberDeclarationContext : ctx.memberDeclaration()) {
            classBodyNode.addMemberDeclarationNode(visitMemberDeclaration(memberDeclarationContext));
        }
        return classBodyNode;
    }

    @Override
    public MemberDeclarationNode visitMemberDeclaration(MxParser.MemberDeclarationContext ctx) {
        if (ctx.constructorDeclaration() != null) {
            // System.out.println("Asdas");
            return new ConstructorDeclarationNode(visitConstructorDeclaration(ctx.constructorDeclaration()));
        }
        else if (ctx.methodDeclaration() != null) {
            return new MemberMethodDeclarationNode(visitMethodDeclaration(ctx.methodDeclaration()));
        }
        else if (ctx.variableDeclaration() != null) {
            return new MemberVariableDeclarationNode(visitVariableDeclaration(ctx.variableDeclaration()));
        }
        else return null;
    }

    @Override
    public MethodNode visitMethodDeclaration(MxParser.MethodDeclarationContext ctx) {
        TypeNode typeNode;
        if (ctx.typeSpec() != null)
            typeNode = visitTypeSpec(ctx.typeSpec());
        else
            typeNode = new TypeNode(new VoidTypeRef(getLocation(ctx)));
        String name = ctx.Identifier().getText();
        ParameterListNode parameterListNode = visitParameters(ctx.parameters());
        BlockNode block = visitBlock(ctx.block());
        return new MethodNode(typeNode, name, parameterListNode.getParameterNodes(), block);
    }

    @Override
    public ParameterListNode visitParameters(MxParser.ParametersContext ctx) {
        if (ctx.parameterList() != null) {
            return visitParameterList(ctx.parameterList());
        }
        else {
            return new ParameterListNode();
        }
    }

    @Override
    public ParameterListNode visitParameterList(MxParser.ParameterListContext ctx) {
        ParameterListNode parameterListNode = new ParameterListNode();
        for (MxParser.ParameterContext parameterContext : ctx.parameter()) {
            parameterListNode.addParameterNode(visitParameter(parameterContext));
        }
        return parameterListNode;
    }

    @Override
    public ParameterNode visitParameter(MxParser.ParameterContext ctx) {
        TypeNode type = visitTypeSpec(ctx.typeSpec());
        VariableDeclaratorIdNode variableDeclaratorIdNode = visitVariableDeclaratorId(ctx.variableDeclaratorId());
        return new ParameterNode(type, variableDeclaratorIdNode.getName());
    }

    @Override
    public ConstructorNode visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        String name = ctx.Identifier().getText();
        ParameterListNode parameterListNode = visitParameters(ctx.parameters());
        BlockNode block = visitBlock(ctx.block());
        return new ConstructorNode(getLocation(ctx), name, parameterListNode.getParameterNodes(), block);
    }

    @Override
    public BlockNode visitBlock(MxParser.BlockContext ctx) {
        BlockNode blockNode = new BlockNode(getLocation(ctx));
        for (MxParser.BlockStatementContext blockStatementContext : ctx.blockStatement()) {
            blockNode.addStatement(visitBlockStatement(blockStatementContext));
        }
        return blockNode;
    }

    @Override
    public StatementNode visitBlockStatement(MxParser.BlockStatementContext ctx) {
        if (ctx.localVariableDeclarationStatement() != null) {
            return visitLocalVariableDeclarationStatement(ctx.localVariableDeclarationStatement());
        }
        else {
            return visitStatement(ctx.statement());
        }
    }

    @Override
    public StatementNode visitStatement(MxParser.StatementContext ctx) {
        Location location = getLocation(ctx);
        if (ctx.block() != null) {                                          // block
            return visitBlock(ctx.block());
        }
        else if (ctx.getChild(0) instanceof TerminalNode) {
            String token = ctx.getChild(0).getText();
            switch (token) {
                case "if":
                    ExpressionNode ifJudge = visitExpression(ctx.expression());
                    StatementNode thenStatement = visitStatement(ctx.statement(0));
                    if (ctx.statement().size() > 1) {
                        StatementNode elseStatement = visitStatement(ctx.statement(1));
                        return new IfNode(location, ifJudge, thenStatement, elseStatement);
                    }
                    else {
                        return new IfNode(location, ifJudge, thenStatement);
                    }
                case "for":
                    ExpressionNode forInit = ctx.forInit() == null ? null : visitForInit(ctx.forInit());
                    ExpressionNode forJudge = ctx.expression() == null ? null : visitExpression(ctx.expression());
                    ExpressionNode forUpdate = ctx.forUpdate() == null ? null : visitForUpdate(ctx.forUpdate());
                    StatementNode forBody = visitStatement(ctx.statement(0));
                    return new ForNode(location, forInit, forJudge, forUpdate, forBody);
                case "while":
                    ExpressionNode whileJudge = visitExpression(ctx.expression());
                    StatementNode body = visitStatement(ctx.statement(0));
                    return new WhileNode(location, whileJudge, body);
                case "return":
                    if (ctx.expression() == null)
                        return new ReturnNode(location);
                    else
                        return new ReturnNode(location, visitExpression(ctx.expression()));
                case "break":
                    return new BreakNode(location);
                case "continue":
                    return new ContinueNode(location);
                default:
                    return new NullStatementNode(location);
            }
        }
        else {                                                              // expression;
            return new ExpressionStatementNode(visitExpression(ctx.expression()));
        }
    }

    @Override
    public ExpressionNode visitForInit(MxParser.ForInitContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public ExpressionNode visitForUpdate(MxParser.ForUpdateContext ctx) {
        return visitExpression(ctx.expression());
    }

    @Override
    public LocalVariableDeclarationNode visitLocalVariableDeclarationStatement(MxParser.LocalVariableDeclarationStatementContext ctx) {
        return visitLocalVariableDeclaration(ctx.localVariableDeclaration());
    }

    @Override
    public LocalVariableDeclarationNode visitLocalVariableDeclaration(MxParser.LocalVariableDeclarationContext ctx) {
        TypeNode typeNode = visitTypeSpec(ctx.typeSpec());
        VariableDeclaratorNode variableDeclaratorNode = visitVariableDeclarator(ctx.variableDeclarator());
        return new LocalVariableDeclarationNode(typeNode, variableDeclaratorNode.getName(), variableDeclaratorNode.getInitExpr());
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
