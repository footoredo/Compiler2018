package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.BuiltinFunction;
import cat.footoredo.mx.entity.DefinedFunction;
import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.Variable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Declarations {
    private Set<Variable> vars = new LinkedHashSet<>();
    private Set<BuiltinFunction> builtinFuns = new LinkedHashSet<>();
    private Set<DefinedFunction> funs = new LinkedHashSet<>();
    private Set<TypeDefinition> typeDefinitions = new LinkedHashSet<>();

    public void addVar (Variable var) {
        vars.add (var);
    }

    public void addVars (List<Variable> _vars) {
        vars.addAll(_vars);
    }

    public void addFun (DefinedFunction fun) {
        funs.add (fun);
    }

    public void addFuns (List<DefinedFunction> _funs) {
        funs.addAll(_funs);
    }

    public void addFun (BuiltinFunction fun) {
        builtinFuns.add (fun);
    }

    public void addBuiltinFuns (List<BuiltinFunction> _funs) {
        builtinFuns.addAll(_funs);
    }

    public void addTypeDefinition (TypeDefinition typeDefinition) {
        typeDefinitions.add (typeDefinition);
    }

    public void addTypeDefinitions (List<ClassNode> typeDefinitions) {
        this.typeDefinitions.addAll (typeDefinitions);
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

    public List<TypeDefinition> getTypeDefinitions () {
        return new ArrayList<>(typeDefinitions);
    }
}
