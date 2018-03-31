package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.*;
import org.junit.Before;
import org.junit.Test;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;

import static org.junit.Assert.*;

public class LocalResolverTest {
    private ParseTree tree;

    @Before
    public void setUp() throws Exception {
        // InputStream is = new FileInputStream("example/test.m"); // or System.in;
        CharStream input = CharStreams.fromFileName("example/test.m");
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        tree = parser.compilationUnit();
    }

    @Test
    public void visitCompilationUnit() {
        LocalResolver localResolver = new LocalResolver();
        localResolver.visit(tree);
    }
}