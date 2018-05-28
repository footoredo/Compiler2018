package cat.footoredo.mx.OJ;

import cat.footoredo.mx.compiler.Compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tester {
    public static void main(String[] args) throws Exception {
        Compiler compiler = new Compiler();
        compiler.compile("program.m", "program.asm");
    }

    private static int exec (String command, boolean error) throws Exception {
        Process process = Runtime.getRuntime().exec(new String[] {"sh", "-c", command});
        int ret = process.waitFor();

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(process.getErrorStream()));

        // System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

// read any errors from the attempted command
        // System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        if (ret != 0 && error)
            throw new Exception("error when exec");

        return ret;
    }

    public static void compileAndRun (String src) throws Exception {
        String srcPath = "./example/" + src;
        Compiler compiler = new Compiler();
        compiler.compile(srcPath + ".m", srcPath + ".asm");
        Runtime runtime = Runtime.getRuntime();

        exec("nasm -felf64 " + srcPath + ".asm -o " + srcPath + ".o", true);
        exec("gcc " + srcPath + ".o -o " + srcPath, true);
        System.out.println("Return value: " + exec(srcPath, false));
    }
}
