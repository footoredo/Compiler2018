package cat.footoredo.mx.ir;

import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.entity.Variable;

import java.util.ArrayList;
import java.util.List;

public class IR {
    private Location location;
    private ToplevelScope scope;
    private ConstantTable constantTable;

    public IR(Location location,
              ToplevelScope scope, ConstantTable constantTable,
              List<Statement> globalStatements) {
        this.location = location;
        this.scope = scope;
        this.constantTable = constantTable;
        ((DefinedFunction)scope.get("main")).appendFront(globalStatements);
    }

    public Location getLocation() {
        return location;
    }

    public List<Variable> getVariables() {
        return scope.getVariables();
    }

    public List<DefinedFunction> getDefinedFunctions() {
        return scope.getDefinedFunctions();
    }

    public List<DefinedFunction> getAllDefinedFunctions () {
        return scope.getAllDefinedFunctions();
    }

    public List<BuiltinFunction> getBuiltinFunctions() {
        return scope.getBuiltinFunctions();
    }

    public List<Function> getAllFunctions () {
        ArrayList<Function> result = new ArrayList<>();
        result.addAll (getDefinedFunctions());
        result.addAll (getBuiltinFunctions());
        return result;
    }

    public ToplevelScope getScope() {
        return scope;
    }

    public ConstantTable getConstantTable() {
        return constantTable;
    }

    public List<Variable> getStaticVariables () {
        return scope.getStaticVariables();
    }

    public List<Variable> getNonstaticVariables () {
        return scope.getNonstaticVariables();
    }
}
