package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticError;

import java.util.LinkedList;
import java.util.List;

public class LocalResolver extends Visitor {
    private LinkedList<Scope> scopeStack;

    public LocalResolver () {
        this.scopeStack = new LinkedList<>();
    }

    public void resolve (AST ast) throws SemanticError {
        ToplevelScope toplevelScope = new ToplevelScope();
        scopeStack.add(toplevelScope);

        for (Entity decl : ast.getDeclarations()) {
            toplevelScope.declareEntity(decl);
        }

        resolveGvarInitializers(ast.getVariables());
    }

    private void resolve (StatementNode node) {
        node.accept(this);
    }

    private void resolve (ExpressionNode node) {
        node.accept(this);
    }

    private void resolveGvarInitializers(List<Variable> vars) {
        for (Variable v: vars) {
            if (v.hasInitializer()) {
                resolve(v.getInitializer());
            }
        }
    }
}
