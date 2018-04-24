package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Declarations {
    private Set<Variable> vars = new LinkedHashSet<>();
    private Set<BuiltinFunction> builtinFuns = new LinkedHashSet<>();
    private Set<DefinedFunction> funs = new LinkedHashSet<>();
    private Set<ClassNode> classes = new LinkedHashSet<>();
    private Set<BuiltinTypeNode> builtinTypeNodes = new LinkedHashSet<>();
    private List<Entity> entities = new ArrayList<>();

    public void addVar (Variable var) {
        vars.add (var);
        entities.add(var);
    }

    public void addVars (List<Variable> _vars) {
        vars.addAll(_vars);
        entities.addAll(_vars);
    }

    public void addFun (DefinedFunction fun) {
        funs.add (fun);
        entities.add(fun);
    }

    public void addFuns (List<DefinedFunction> _funs) {
        funs.addAll(_funs);
        entities.addAll(_funs);
    }

    public void addFun (BuiltinFunction fun) {
        builtinFuns.add (fun);
        entities.add(fun);
    }

    public void addBuiltinFuns (List<BuiltinFunction> _funs) {
        builtinFuns.addAll(_funs);
        entities.addAll(_funs);
    }

    public void addTypeDefinition (ClassNode typeDefinition) {
        classes.add (typeDefinition);
    }

    public void addTypeDefinition (BuiltinTypeNode typeDefinition) {
        builtinTypeNodes.add (typeDefinition);
    }

    /*public void addTypeDefinitions (List<TypeDefinition> typeDefinitions) {
        classes.addAll (typeDefinitions);
    }*/

    public List<Entity> getDeclarations() {
        return entities;
    }

    public List<Variable> getVars () {
        return new ArrayList<> (vars);
    }

    public List<DefinedFunction> getFuns () {
        return new ArrayList<> (funs);
    }

    public List<BuiltinFunction> getBuiltinFuns () {
        return new ArrayList<> (builtinFuns);
    }

    public List<ClassNode> getClasses() {
        return new ArrayList<>(classes);
    }

    public List<BuiltinTypeNode> getBuiltinTypes() {
        return new ArrayList<>(builtinTypeNodes);
    }
}
