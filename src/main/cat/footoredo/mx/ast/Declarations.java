package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.*;

import java.util.*;

public class Declarations {
    private Set<Variable> vars = new LinkedHashSet<>();
    private Set<BuiltinFunction> builtinFuns = new LinkedHashSet<>();
    private Set<DefinedFunction> funs = new LinkedHashSet<>();
    //private Set<ClassNode> classes = new LinkedHashSet<>();
    //private Set<BuiltinTypeNode> builtinTypeNodes = new LinkedHashSet<>();
    private Map<String, ClassNode> classes = new HashMap<>();
    private Map<String, BuiltinTypeNode> builtinTypes = new HashMap<>();
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
        classes.put (typeDefinition.getName(), typeDefinition);
    }

    public void addTypeDefinition (BuiltinTypeNode typeDefinition) {
        builtinTypes.put (typeDefinition.getName(), typeDefinition);
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
        return new ArrayList<>(classes.values());
    }

    public List<BuiltinTypeNode> getBuiltinTypes() {
        return new ArrayList<>(builtinTypes.values());
    }

    public TypeDefinition getTypeDefinition (String name) {
        if (builtinTypes.containsKey(name)) {
            return builtinTypes.get(name);
        }
        else {
            return classes.get(name);
        }
    }
}
