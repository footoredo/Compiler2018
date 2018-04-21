package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.IntegerTypeRef;

import java.util.LinkedList;
import java.util.List;

public class LocalResolver extends Visitor {
    private LinkedList<Scope> scopeStack;

    public LocalResolver () {
        this.scopeStack = new LinkedList<>();
    }

    public void resolve (AST ast) throws SemanticException {
        ToplevelScope toplevelScope = new ToplevelScope();
        scopeStack.add(toplevelScope);

        for (Entity decl : ast.getDeclarations()) {
            toplevelScope.declareEntity(decl);
        }

        resolveGvarInitializers(ast.getVariables());
        resolveFunctions(ast.getFunctions());

        Entity main = toplevelScope.get("main");
        // System.out.println(main.getTypeNode().getTypeRef().toString());
        if (main == null ||
                !(main instanceof DefinedFunction &&
                        main.getTypeNode().getTypeRef() instanceof IntegerTypeRef)) {
            // System.out.println("asdasd");
            throw new SemanticException("no main function found");
        }

        ast.setScop(toplevelScope);
    }

    private void resolveGvarInitializers(List<Variable> vars) {
        for (Variable v: vars) {
            if (v.hasInitializer()) {
                visitExpression(v.getInitializer());
            }
        }
    }

    private void resolveFunctions(List<DefinedFunction> funs) {
        for (DefinedFunction f: funs) {
            pushScope(f.getParameters());
            super.visit(f.getBlock());
            f.setScope(popScope());
        }
    }

    @Override
    public Void visit(BlockNode node) {
        pushScope();
        super.visit(node);
        node.setScope(popScope());
        return null;
    }

    @Override
    public Void visit(LocalVariableDeclarationNode node) {
        super.visit(node);
        ((LocalScope) currentScope()).defineVariable(node.getVariable());
        return null;
    }

    private void pushScope (List <? extends Variable> vars) {
        LocalScope scope = new LocalScope(currentScope());
        for (Variable var: vars) {
            scope.defineVariable(var);
        }
        scopeStack.addLast(scope);
    }

    private void pushScope () {
        LocalScope scope = new LocalScope(currentScope());
        scopeStack.addLast(scope);
    }

    private LocalScope popScope () {
        return (LocalScope) scopeStack.removeLast();
    }

    private Scope currentScope() {
        return scopeStack.getLast();
    }

    @Override
    public Void visit(VariableNode node) {
        Entity entity = currentScope().get(node.getName());
        entity.referred();
        node.setEntity(entity);
        return null;
    }
}
