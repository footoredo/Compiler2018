package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AST extends Node {
    protected Location location;
    private Declarations declarations;
    private ToplevelScope scop;

    AST (Location location, Declarations declarations) {
        super ();
        this.location = location;
        this.declarations = declarations;
    }

    public Location getLocation() {
        return location;
    }

    public List<Entity> getDeclarations() {
        List<Entity> result = new ArrayList<>();
        result.addAll(declarations.getFuns());
        result.addAll(declarations.getBuiltinFuns());
        result.addAll(declarations.getVars());
        return result;
    }

    public List<Variable> getVariables() {
        return declarations.getVars();
    }

    public List<DefinedFunction> getFunctions() { return declarations.getFuns(); }

    public void addFunction(DefinedFunction fun) {
        declarations.addFun(fun);
    }

    public void addFunction(BuiltinFunction fun) {
        declarations.addFun(fun);
    }

    public ToplevelScope getScop() {
        return scop;
    }

    public void setScop(ToplevelScope scop) {
        this.scop = scop;
    }
}
