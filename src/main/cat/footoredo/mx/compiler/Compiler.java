package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.AST;
import cat.footoredo.mx.ast.ASTBuilderVisitor;
import cat.footoredo.mx.ast.BuiltinTypeNode;
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
        AST sem = semanticAnalyze(ast_builtin_types);
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

    AST semanticAnalyze(AST ast) throws SemanticException {
        new LocalResolver().resolve(ast);
        new TypeResolver().resolve(ast);
        return ast;
    }

    AST addBuiltinFunctions(AST ast) throws IOException {
        CharStream input = CharStreams.fromFileName("builtin/global_builtin.m");
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        // parser.setErrorHandler(new MxANTLRErrorStrategy());
        parser.setErrorHandler(new DefaultErrorStrategy());
        ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
        List<BuiltinFunction> builtinDeclarations = astBuilderVisitor.visitBuiltinDeclarations(parser.builtinDeclarations()).getFunctions();
        for (BuiltinFunction function: builtinDeclarations) {
            ast.addFunction(function);
        }
        return ast;
    }

    AST addBuiltinTypes(AST ast) {
        ast.addTypeDefinition(new BuiltinTypeNode(new IntegerTypeRef()));
        ast.addTypeDefinition(new BuiltinTypeNode(new BooleanTypeRef()));
        ast.addTypeDefinition(new BuiltinTypeNode(new StringTypeRef()));
        ast.addTypeDefinition(new BuiltinTypeNode(new VoidTypeRef()));
        return ast;
    }
}
