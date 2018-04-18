package cat.footoredo.mx.compiler;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompilerTest {
    Compiler compiler;
    @Before
    public void setUp() throws Exception {
        compiler = new Compiler();
    }

    @Test
    public void testLocalResolver() {
        try {
            compiler.compile("example/test_local_resolver.m");
        } catch (Exception e) {
            System.out.println("Error occurred:");
            System.out.println(e);
        }
    }
}