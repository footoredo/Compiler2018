package cat.footoredo.mx.compiler;

import cat.footoredo.mx.ast.*;
import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.ClassType;
import cat.footoredo.mx.type.ClassTypeRef;
import cat.footoredo.mx.type.IntegerTypeRef;
import cat.footoredo.mx.type.StringType;

import java.util.*;

public class LocalResolver extends Visitor {
    private final LinkedList<Scope> scopeStack;
    private final ConstantTable constantTable;
    private final Set<String> reservedWords = new HashSet<>();
    private ClassNode currentClass;

    public LocalResolver (String[] reservedWords) {
        this.scopeStack = new LinkedList<>();
        this.constantTable = new ConstantTable();
        this.reservedWords.addAll(Arrays.asList(reservedWords));
    }

    public void addReservedWords(String name) {
        reservedWords.add(name);
    }

    public void resolve (AST ast) throws SemanticException {
        currentClass = null;
        ToplevelScope toplevelScope = new ToplevelScope();
        scopeStack.add(toplevelScope);

        for (Entity decl : ast.getDeclarations()) {
            // System.out.println(decl.getName());
            if (decl instanceof Variable) continue;
            toplevelScope.declareEntity(decl, reservedWords);
        }

        for (Entity decl : ast.getDeclarations()) {
            if (decl instanceof Variable) {
                toplevelScope.declareEntity(decl, reservedWords);
                resolveGvarInitializer((Variable) decl);
            }
            else if (decl instanceof DefinedFunction){
                resolveFunction((DefinedFunction) decl);
            }
        }

        // resolveDefinedFunctions(ast.getFunctions());
        resolveTypeDefinition(ast.getTypeDefinitions());

        try {
            Entity main = toplevelScope.get("main");
            if (!(main instanceof DefinedFunction &&
                    ((DefinedFunction)main).getReturnTypeNode().isInteger())) {
                throw new SemanticException("no main function found");
            }
        } catch (SemanticException e) {
            throw new SemanticException("no main function found");
        }

        ast.setScope(toplevelScope);
        ast.setConstantTable(constantTable);
    }

    private void resolveGvarInitializer(Variable var) {
        if (var.hasInitializer()) {
            visitExpression(var.getInitializer());
        }
    }

    private void resolveFunction(DefinedFunction df) {
        pushScope(df.getParameters());
        this.visit(df.getBlock());
        df.setScope(popScope());
    }

    private void resolveFunctions(List<Function> funs) {
        for (Function f: funs) {
            if (f instanceof DefinedFunction) {
                resolveFunction((DefinedFunction)f);
            }
        }
    }

    private void resolveDefinedFunctions(List<DefinedFunction> funs) {
        for (DefinedFunction f: funs) {
            resolveFunction(f);
        }
    }

    private void resolveTypeDefinition(List<TypeDefinition> typeDefinitions) {
        for (TypeDefinition t: typeDefinitions) {
            if (t instanceof ClassNode) {
                currentScope().declareEntity(((ClassNode)t).getConstructor());
            }
            pushScope(t.getMemberVariables());
            currentScope().declareEntity(new Variable(t.getTypeNode(), "this"));
            for (Function function: t.getMemberMethods())
                currentScope().declareEntity(function, reservedWords);
            if (t instanceof ClassNode) {
                ClassNode c = (ClassNode) t;
                if (c.hasConstructor()) {
                    if (!c.getConstructor().getName().equals(c.getName()))
                        throw new SemanticException("constructor name incompatible with class name");
                }
                currentClass = c;
            }
            resolveFunctions(t.getMemberMethods());
            if (t instanceof ClassNode) {
                currentClass = null;
            }
            t.setScope(popScope());
        }
    }

    @Override
    public Void visit(BlockNode node) {
        pushScope();
        // System.out.println("BlockNode " + node.getLocation());
        super.visit(node);
        node.setScope(popScope());
        return null;
    }

    @Override
    public Void visit(LocalVariableDeclarationNode node) {
        super.visit(node);
        ((LocalScope) currentScope()).declareEntity(node.getVariable(), reservedWords);
        return null;
    }

    @Override
    public Void visit(NewNode node) {
        super.visit(node);
        resolveCreator (node.getCreator());
        return null;
    }

    @Override
    public Void visit(StringLiteralNode node) {
        node.setEntry(constantTable.intern(node.getValue()));;
        return null;
    }

    private void resolveCreator (CreatorNode node) {
        if (node.hasArgs()) {
            DefinedFunction constructor = (DefinedFunction) (currentScope().get (node.getClassName()));
            node.setConstructor(constructor);
        }
    }

    private void pushScope (List <? extends Variable> vars) {
        LocalScope scope = new LocalScope(currentScope());
        for (Variable var: vars) {
            scope.declareEntity(var, reservedWords);
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
        // System.out.println("resolving variable " + node.getName());
        if (currentClass != null && currentScope().directGet(node.getName()) == null &&
                currentClass.hasMemberVariable(node.getName())) {
            node.setMember(true);
        }
        Entity entity = currentScope().get(node.getName());
        entity.referred();
        node.setEntity(entity);
        // System.out.println(entity.getTypeNode());
        return null;
    }
}
