package cat.footoredo.mx.OJ;

import cat.footoredo.mx.compiler.Compiler;

public class Semantic {
    public static void main(String[] args) throws Exception {
        Compiler compiler = new Compiler();
        compiler.compile("program.m");
    }
}
