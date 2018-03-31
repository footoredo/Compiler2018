package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.*;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.*;

public class LocalResolver implements MxVisitor <Void> {
    @Override
    public Void visitCompilationUnit(MxParser.CompilationUnitContext ctx) {
        System.out.println ("Hello mx!");
        return null;
    }

    @Override
    public Void visitGlobalDeclaration(MxParser.GlobalDeclarationContext ctx) {
        return null;
    }

    @Override
    public Void visitTypeDeclaration(MxParser.TypeDeclarationContext ctx) {
        return null;
    }

    @Override
    public Void visitMainDeclaration(MxParser.MainDeclarationContext ctx) {
        return null;
    }

    @Override
    public Void visitExpression(MxParser.ExpressionContext ctx) {
        return null;
    }

    @Override
    public Void visitExpressionList(MxParser.ExpressionListContext ctx) {
        return null;
    }

    @Override
    public Void visitCreator(MxParser.CreatorContext ctx) {
        return null;
    }

    @Override
    public Void visitCreatedName(MxParser.CreatedNameContext ctx) {
        return null;
    }

    @Override
    public Void visitArrayCreatorRest(MxParser.ArrayCreatorRestContext ctx) {
        return null;
    }

    @Override
    public Void visitClassCreatorRest(MxParser.ClassCreatorRestContext ctx) {
        return null;
    }

    @Override
    public Void visitArguements(MxParser.ArguementsContext ctx) {
        return null;
    }

    @Override
    public Void visitPrimary(MxParser.PrimaryContext ctx) {
        return null;
    }

    @Override
    public Void visitLiteral(MxParser.LiteralContext ctx) {
        return null;
    }

    @Override
    public Void visitBoolLiteral(MxParser.BoolLiteralContext ctx) {
        return null;
    }

    @Override
    public Void visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        return null;
    }

    @Override
    public Void visitVariableDeclarators(MxParser.VariableDeclaratorsContext ctx) {
        return null;
    }

    @Override
    public Void visitVariableDeclarator(MxParser.VariableDeclaratorContext ctx) {
        return null;
    }

    @Override
    public Void visitVariableDeclaratorId(MxParser.VariableDeclaratorIdContext ctx) {
        return null;
    }

    @Override
    public Void visitVariableInitializer(MxParser.VariableInitializerContext ctx) {
        return null;
    }

    @Override
    public Void visitPrimitiveType(MxParser.PrimitiveTypeContext ctx) {
        return null;
    }

    @Override
    public Void visitTypeSpec(MxParser.TypeSpecContext ctx) {
        return null;
    }

    @Override
    public Void visitClassType(MxParser.ClassTypeContext ctx) {
        return null;
    }

    @Override
    public Void visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        return null;
    }

    @Override
    public Void visitClassBody(MxParser.ClassBodyContext ctx) {
        return null;
    }

    @Override
    public Void visitMemberDeclaration(MxParser.MemberDeclarationContext ctx) {
        return null;
    }

    @Override
    public Void visitMethodDeclaration(MxParser.MethodDeclarationContext ctx) {
        return null;
    }

    @Override
    public Void visitParameters(MxParser.ParametersContext ctx) {
        return null;
    }

    @Override
    public Void visitParametersList(MxParser.ParametersListContext ctx) {
        return null;
    }

    @Override
    public Void visitParameter(MxParser.ParameterContext ctx) {
        return null;
    }

    @Override
    public Void visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx) {
        return null;
    }

    @Override
    public Void visitBlock(MxParser.BlockContext ctx) {
        return null;
    }

    @Override
    public Void visitBlockStatement(MxParser.BlockStatementContext ctx) {
        return null;
    }

    @Override
    public Void visitStatement(MxParser.StatementContext ctx) {
        return null;
    }

    @Override
    public Void visitForControl(MxParser.ForControlContext ctx) {
        return null;
    }

    @Override
    public Void visitForInit(MxParser.ForInitContext ctx) {
        return null;
    }

    @Override
    public Void visitForUpdate(MxParser.ForUpdateContext ctx) {
        return null;
    }

    @Override
    public Void visitLocalVariableDeclarationStatement(MxParser.LocalVariableDeclarationStatementContext ctx) {
        return null;
    }

    @Override
    public Void visitLocalVariableDeclaration(MxParser.LocalVariableDeclarationContext ctx) {
        return null;
    }

    @Override
    public Void visit(ParseTree parseTree) {
        parseTree.accept(this);
        return null;
    }

    @Override
    public Void visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override
    public Void visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public Void visitErrorNode(ErrorNode errorNode) {
        return null;
    }
}
