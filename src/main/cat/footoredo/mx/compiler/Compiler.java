package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.AST;
import cat.footoredo.mx.ast.ASTBuilderVisitor;
import cat.footoredo.mx.cst.MxLexer;
import cat.footoredo.mx.cst.MxParser;
import cat.footoredo.mx.exception.SemanticError;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;

import java.io.IOException;

public class Compiler {
    public void compile(String strPath) throws IOException, SemanticError {
        AST ast = parseFile(strPath);
        AST sem = semanticAnalyze(ast);
    }

    public AST parseFile(String path) throws IOException {
        CharStream input = CharStreams.fromFileName(path);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        // parser.setErrorHandler(new MxANTLRErrorStrategy());
        parser.setErrorHandler(new DefaultErrorStrategy());
        ASTBuilderVisitor astBuilderVisitor = new ASTBuilderVisitor();
        AST ast = astBuilderVisitor.visitCompilationUnit(parser.compilationUnit());
        return ast;
    }

    public AST semanticAnalyze(AST ast) throws SemanticError {
        new LocalResolver().resolve(ast);
        return ast;
    }
}
