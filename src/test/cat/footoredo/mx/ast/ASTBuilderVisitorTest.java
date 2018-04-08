package cat.footoredo.mx.ast;

import cat.footoredo.mx.cst.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
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
    }

    @Test
    public void visitCompilationUnit() {
        ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor ();
        AST ast = astBuilderVisitor.visitCompilationUnit (parser.compilationUnit());
    }
}