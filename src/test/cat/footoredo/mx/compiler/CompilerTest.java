package cat.footoredo.mx.compiler;

import cat.footoredo.mx.OJ.Semantic;
import cat.footoredo.mx.exception.SemanticError;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CompilerTest {
    Compiler compiler;
    @Before
    public void setUp() throws Exception {
        compiler = new Compiler();
    }

    @Test
    public void testLocalResolver() throws IOException {
        try {
            compiler.compile("example/test_local_resolver.m");
        } catch (SemanticError e) {
            System.out.println("Error occurred:");
            System.out.println(e);
        }
    }

    @Test
    public void T698() throws Exception {
        try {
            compiler.compile("example/T698.m");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void T690() throws Exception {
        try {
            compiler.compile("example/T690.m");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}