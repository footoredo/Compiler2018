package cat.footoredo.mx.compiler;

import cat.footoredo.mx.asm.Assembly;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.cfg.CFG;
import cat.footoredo.mx.cfg.RegisterAllocator;
import cat.footoredo.mx.cst.MxLexer;
import cat.footoredo.mx.cst.MxParser;
import cat.footoredo.mx.entity.BuiltinFunction;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.ir.IR;
import cat.footoredo.mx.sysdep.x86_64.AssemblyCode;
import cat.footoredo.mx.sysdep.x86_64.CodeGenerator;
import cat.footoredo.mx.type.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;

import javax.annotation.processing.FilerException;
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Compiler {
    public void compile(String strPath, String destPath) throws Exception {
        AST ast = parseFile(strPath);
        AST ast_builtin_functions = addBuiltinFunctions(ast);
        AST ast_builtin_types = addBuiltinTypes(ast);
        TypeTable types = new TypeTable();
        AST sem = semanticAnalyze(ast_builtin_types, types);
        IR ir = new IRGenerator(types).generate(sem);
        CFG cfg = new CFGBuilder().generateCFG(ir);
        AssemblyCode asm = generateAssembly(ir, cfg);
        String code = asm.toSource();
        code += readFile("builtin/global_builtin.asm");
        writeFile(destPath, code);
    }

    public void compile(String strPath) throws Exception {
        compile(strPath, "-");
    }

    String readFile(String path) throws IOException {
        return new Scanner(new File(path)).useDelimiter("\\Z").next();
    }

    AST parseFile(String path) throws IOException {
        CharStream input = CharStreams.fromFileName(path);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        // System.out.println("here");
        // parser.setErrorHandler(new MxANTLRErrorStrategy());
        parser.setErrorHandler(new DefaultErrorStrategy());
        ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
        try {
            AST ast = astBuilderVisitor.visitCompilationUnit(parser.compilationUnit());
            return ast;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void writeFile (String path, String content) throws Exception {
        if (path.equals("-")) {
            System.out.print(content);
            return;
        }
        try {
            BufferedWriter f = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(path)));
            try {
                f.write(content);
            }
            finally {
                f.close();
            }
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    AST semanticAnalyze(AST ast, TypeTable types) throws SemanticException {
        String[] reservedWords = {"bool", "int", "string", "null", "void", "true", "false",
            "if", "for", "while", "break", "continue", "return", "new", "class", "this"};
        new LocalResolver(reservedWords).resolve(ast);
        new TypeResolver(types).resolve(ast);
        types.semanticCheck();
        new TypeChecker(types).check(ast);
        return ast;
    }

    AST addBuiltinFunctions(AST ast) throws IOException {
        List<BuiltinFunction> builtinDeclarations = getBuiltinDeclarations("builtin/global_builtin.m");
        for (BuiltinFunction function: builtinDeclarations) {
            ast.addFunction(function);
        }
        return ast;
    }

    List<BuiltinFunction> getBuiltinDeclarations(String filename, String parentClass) throws IOException {
        CharStream input = CharStreams.fromFileName(filename);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        // parser.setErrorHandler(new MxANTLRErrorStrategy());
        parser.setErrorHandler(new DefaultErrorStrategy());
        ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
        List<BuiltinFunction> result = astBuilderVisitor.visitBuiltinDeclarations(parser.builtinDeclarations()).getFunctions();
        for (BuiltinFunction function: result) {
            function.setParentClass (parentClass);
        }
        return result;
    }

    List<BuiltinFunction> getBuiltinDeclarations(String filename) throws IOException {
        return getBuiltinDeclarations(filename, null);
    }


    AST addBuiltinTypes(AST ast) throws IOException {
        ast.addTypeDefinition(new BuiltinTypeNode(new IntegerTypeRef()));
        ast.addTypeDefinition(new BuiltinTypeNode(new BooleanTypeRef()));
        ast.addTypeDefinition(new BuiltinTypeNode(new VoidTypeRef()));
        ast.addTypeDefinition(new BuiltinTypeNode(new NullTypeRef()));
        ast.addTypeDefinition(new ProtoArrayTypeNode(getBuiltinDeclarations("builtin/array_builtin.m", "__array")));
        ast.addTypeDefinition(new StringTypeNode(getBuiltinDeclarations("builtin/string_builtin.m", "string")));
        return ast;
    }

    AssemblyCode generateAssembly (IR ir, CFG cfg) {
        return new CodeGenerator(Type.INT64).generate(ir, cfg);
    }
}
