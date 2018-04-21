package cat.footoredo.mx.ast;

import cat.footoredo.mx.cst.*;
import cat.footoredo.mx.exception.MxANTLRErrorStrategy;
import org.antlr.v4.runtime.*;
import org.junit.Test;

public class ASTBuilderVisitorTest {
    @Test
    public void testVariableDefinition() {
        try {
            CharStream input = CharStreams.fromFileName("example/test_variable_definition.m");
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            parser.setErrorHandler(new MxANTLRErrorStrategy());
            ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
            AST ast = astBuilderVisitor.visitCompilationUnit(parser.compilationUnit());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testNewDeclarator() {
        try {
            CharStream input = CharStreams.fromFileName("example/test_new_declarator.m");
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            parser.setErrorHandler(new MxANTLRErrorStrategy());
            ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
            AST ast = astBuilderVisitor.visitCompilationUnit(parser.compilationUnit());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testFunctionDeclaration() {
        try {
            CharStream input = CharStreams.fromFileName("example/test_function_declaration.m");
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            // parser.setErrorHandler(new MxANTLRErrorStrategy());
            parser.setErrorHandler(new DefaultErrorStrategy());
            ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
            AST ast = astBuilderVisitor.visitCompilationUnit(parser.compilationUnit());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testBlockStatement() {
        try {
            CharStream input = CharStreams.fromFileName("example/test_block_statement.m");
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            // parser.setErrorHandler(new MxANTLRErrorStrategy());
            parser.setErrorHandler(new DefaultErrorStrategy());
            ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
            AST ast = astBuilderVisitor.visitCompilationUnit(parser.compilationUnit());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testOnePlusOneBug() {
        try {
            CharStream input = CharStreams.fromFileName("example/test_one_plus_one_bug.m");
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            // parser.setErrorHandler(new MxANTLRErrorStrategy());
            parser.setErrorHandler(new DefaultErrorStrategy());
            ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
            AST ast = astBuilderVisitor.visitCompilationUnit(parser.compilationUnit());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testClassDeclaration() {
        try {
            CharStream input = CharStreams.fromFileName("example/test_class_declaration.m");
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            parser.setErrorHandler(new MxANTLRErrorStrategy());
            // parser.setErrorHandler(new DefaultErrorStrategy());
            ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
            AST ast = astBuilderVisitor.visitCompilationUnit(parser.compilationUnit());
        } catch (Exception e) {
            System.out.println ("Catched exception.");
            System.out.println(e);
        }
    }
}