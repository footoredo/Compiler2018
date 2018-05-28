package cat.footoredo.mx.compiler;

import cat.footoredo.mx.exception.SemanticException;
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
    public void basicTest() throws Exception {
        compiler.compile("example/basic.m");
    }

    @Test
    public void testLocalResolver() throws Exception {
        try {
            compiler.compile("example/test_local_resolver.m");
        } catch (SemanticException e) {
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
    public void T584() throws Exception {
        try {
            compiler.compile("example/T584.m");
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

    @Test(expected = SemanticException.class)
    public void T671() throws Exception {
        compiler.compile("example/T671.m");
    }

    @Test
    public void T613() throws Exception {
        try {
            compiler.compile("example/T613.m");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void T697() throws Exception {
        try {
            compiler.compile("example/T697.m");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test(expected = SemanticException.class)
    public void testTmp() throws Exception {
        try {
            compiler.compile("example/tmp.m");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test(expected = SemanticException.class)
    public void testClassLocalResolver() throws Exception {
        compiler.compile("example/test_class_local_resolver.m");
    }

    @Test
    public void testTypeResolver() throws Exception {
        compiler.compile("example/test_type_resolver.m");
    }

    @Test
    public void testTypeChecker() throws Exception {
        compiler.compile("example/test_type_checker.m");
    }
}