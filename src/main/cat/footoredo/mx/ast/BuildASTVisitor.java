package cat.footoredo.mx.ast;

import cat.footoredo.mx.cst.MxParser;
import cat.footoredo.mx.cst.MxVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class BuildASTVisitor implements MxVisitor <Node> {
    @Override
    public Node visitCompilationUnit(MxParser.CompilationUnitContext ctx) {
        Declarations declarations = new Declarations();

        for (MxParser.ClassDeclarationContext classDeclarationContext : ctx.classDeclaration()) {
            ClassNode classDeclaration = visitClassDeclaration (classDeclarationContext);
            declarations.addClass(classDeclaration);
        }

        for (MxParser.MethodDeclarationContext methodDeclarationContext : ctx.methodDeclaration()) {

        }
    }

    @Override
    public Node visitExpression(MxParser.ExpressionContext ctx) {
        return null;
    }

    @Override
    public Node visitExpressionList(MxParser.ExpressionListContext ctx) {
        return null;
    }

    @Override
    public Node visitCreator(MxParser.CreatorContext ctx) {
        return null;
    }

    @Override
    public Node visitCreatedName(MxParser.CreatedNameContext ctx) {
        return null;
    }

    @Override
    public Node visitArrayCreatorRest(MxParser.ArrayCreatorRestContext ctx) {
        return null;
    }

    @Override
    public Node visitClassCreatorRest(MxParser.ClassCreatorRestContext ctx) {
        return null;
    }

    @Override
    public Node visitArguements(MxParser.ArguementsContext ctx) {
        return null;
    }

    @Override
    public Node visitPrimary(MxParser.PrimaryContext ctx) {
        return null;
    }

    @Override
    public Node visitLiteral(MxParser.LiteralContext ctx) {
        return null;
    }

    @Override
    public Node visitBoolLiteral(MxParser.BoolLiteralContext ctx) {
        return null;
    }

    @Override
    public Node visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        return null;
    }

    @Override
    public Node visitVariableDeclarators(MxParser.VariableDeclaratorsContext ctx) {
        return null;
    }

    @Override
    public Node visitVariableDeclarator(MxParser.VariableDeclaratorContext ctx) {
        return null;
    }

    @Override
    public Node visitVariableDeclaratorId(MxParser.VariableDeclaratorIdContext ctx) {
        return null;
    }

    @Override
    public Node visitVariableInitializer(MxParser.VariableInitializerContext ctx) {
        return null;
    }

    @Override
    public Node visitPrimitiveType(MxParser.PrimitiveTypeContext ctx) {
        return null;
    }

    @Override
    public Node visitTypeSpec(MxParser.TypeSpecContext ctx) {
        return null;
    }

    @Override
    public Node visitClassType(MxParser.ClassTypeContext ctx) {
        return null;
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
    public Node visitMethodDeclaration(MxParser.MethodDeclarationContext ctx) {
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
