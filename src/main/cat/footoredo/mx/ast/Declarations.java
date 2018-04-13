package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.Variable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Declarations {
    private Set<Variable> vars = new LinkedHashSet<>();
    private Set<Function> funs = new LinkedHashSet<>();
    private Set<ClassNode> classes = new LinkedHashSet<>();

    public void addVar (Variable var) {
        vars.add (var);
    }

    public void addVars (List<Variable> _vars) {
        vars.addAll(_vars);
    }

    public void addFun (Function fun) {
        funs.add (fun);
    }

    public void addFuns (List<Function> _funs) {
        funs.addAll(_funs);
    }

    public void addClass (ClassNode _class) {
        classes.add (_class);
    }

    public void addClasses (List<ClassNode> classes) {
        this.classes.addAll (classes);
    }

    public List<Variable> getVars () {
        return new ArrayList<Variable> (vars);
    }

    public List<Function> getFuns () {
        return new ArrayList<Function> (funs);
    }

    public List<ClassNode> getClasses () {
        return new ArrayList<ClassNode>(classes);
    }
}
