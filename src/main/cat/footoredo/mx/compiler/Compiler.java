package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.AST;
import cat.footoredo.mx.ast.ASTBuilderVisitor;
import cat.footoredo.mx.ast.BuiltinTypeNode;
import cat.footoredo.mx.ast.StringTypeNode;
import cat.footoredo.mx.cst.MxLexer;
import cat.footoredo.mx.cst.MxParser;
import cat.footoredo.mx.entity.BuiltinFunction;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;

import java.io.IOException;
import java.util.List;

public class Compiler {
    public void compile(String strPath) throws IOException, SemanticException {
        AST ast = parseFile(strPath);
        AST ast_builtin_functions = addBuiltinFunctions(ast);
        AST ast_builtin_types = addBuiltinTypes(ast);
        TypeTable types = new TypeTable();
        AST sem = semanticAnalyze(ast_builtin_types, types);
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

    List<BuiltinFunction> getBuiltinDeclarations(String filename) throws IOException {
        CharStream input = CharStreams.fromFileName(filename);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        // parser.setErrorHandler(new MxANTLRErrorStrategy());
        parser.setErrorHandler(new DefaultErrorStrategy());
        ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
        return astBuilderVisitor.visitBuiltinDeclarations(parser.builtinDeclarations()).getFunctions();
    }


    AST addBuiltinTypes(AST ast) throws IOException {
        ast.addTypeDefinition(new BuiltinTypeNode(new IntegerTypeRef()));
        ast.addTypeDefinition(new BuiltinTypeNode(new BooleanTypeRef()));
        ast.addTypeDefinition(new BuiltinTypeNode(new VoidTypeRef()));
        ast.addTypeDefinition(new BuiltinTypeNode(new NullTypeRef()));
        ast.addTypeDefinition(new StringTypeNode(getBuiltinDeclarations("builtin/string_builtin.m")));
        return ast;
    }
}
