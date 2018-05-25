package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.ir.IR;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AST extends Node {
    protected Location location;
    private Declarations declarations;
    private ToplevelScope scope;

    AST (Location location, Declarations declarations) {
        super ();
        this.location = location;
        this.declarations = declarations;
    }

    public Location getLocation() {
        return location;
    }

    public List<Entity> getDeclarations() {
        /*List<Entity> result = new ArrayList<>();
        result.addAll(declarations.getFuns());
        result.addAll(declarations.getBuiltinFuns());
        result.addAll(declarations.getVars());*/;
        return declarations.getDeclarations();
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

    public void addTypeDefinition(ClassNode typeDefinition) {
        declarations.addTypeDefinition(typeDefinition);
    }

    public void addTypeDefinition(BuiltinTypeNode typeDefinition) {
        declarations.addTypeDefinition(typeDefinition);
    }

    public ToplevelScope getScope() {
        return scope;
    }

    public void setScope(ToplevelScope scope) {
        this.scope = scope;
    }

    public List<TypeDefinition> getTypeDefinitions () {
        List<TypeDefinition> ret = new ArrayList<>();
        ret.addAll(declarations.getClasses());
        ret.addAll(declarations.getBuiltinTypes());
        return ret;
    }

    public TypeDefinition getTypeDefinition (String name) {
        return declarations.getTypeDefinition(name);
    }

    public List<ClassNode> getClasses() {
        return declarations.getClasses();
    }

    public List<BuiltinTypeNode> getBuiltinTypes() {
        return declarations.getBuiltinTypes();
    }

    public IR generateIR () {

    }
}
