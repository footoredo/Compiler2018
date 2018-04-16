package cat.footoredo.mx.ast;

import cat.footoredo.mx.cst.*;
import cat.footoredo.mx.exception.MxANTLRErrorStrategy;
import cat.footoredo.mx.exception.ParsingError;
import org.antlr.v4.runtime.*;
import org.junit.Before;
import org.junit.Test;

public class ASTBuilderVisitorTest {

    protected MxParser parser;

    @Before
    public void setUp() throws Exception {
        CharStream input = CharStreams.fromFileName("example/test.m");
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parser = new MxParser(tokens);
        parser.setErrorHandler(new MxANTLRErrorStrategy());
    }

    @Test
    public void visitCompilationUnit() {
        try {
            ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
            AST ast = astBuilderVisitor.visitCompilationUnit(parser.compilationUnit());
        } catch (ParsingError e) {
            System.out.println(e.getMessage());
        }
    }
}