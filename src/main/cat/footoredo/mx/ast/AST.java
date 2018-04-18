package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.Entity;
import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.Location;
import cat.footoredo.mx.entity.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AST extends Node {
    protected Location location;
    protected Declarations declarations;

    public AST (Location location, Declarations declarations) {
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
        result.addAll(declarations.getVars());
        return result;
    }

    public List<Variable> getVariables() {
        return declarations.getVars();
    }

    public List<Function> getFunctions() { return declarations.getFuns(); }
}
