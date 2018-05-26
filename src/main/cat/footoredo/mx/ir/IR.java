package cat.footoredo.mx.ir;

import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.entity.Variable;

import java.util.ArrayList;
import java.util.List;

public class IR {
    private Location location;
    private List<cat.footoredo.mx.entity.Variable> variables;
    private List<DefinedFunction> definedFunctions;
    private List<BuiltinFunction> builtinFunctions;
    private ToplevelScope scope;
    private ConstantTable constantTable;

    private List<cat.footoredo.mx.entity.Variable> globalVariables;
    private List<cat.footoredo.mx.entity.Variable> commonSymbols;

    public IR(Location location, List<Variable> variables, List<DefinedFunction> definedFunctions, List<BuiltinFunction> builtinFunctions, ToplevelScope scope, ConstantTable constantTable) {
        this.location = location;
        this.variables = variables;
        this.definedFunctions = definedFunctions;
        this.builtinFunctions = builtinFunctions;
        this.scope = scope;
        this.constantTable = constantTable;
        initVariables();
    }

    public Location getLocation() {
        return location;
    }

    public List<cat.footoredo.mx.entity.Variable> getVariables() {
        return variables;
    }

    public List<DefinedFunction> getDefinedFunctions() {
        return definedFunctions;
    }

    public List<BuiltinFunction> getBuiltinFunctions() {
        return builtinFunctions;
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

    public List<Variable> getGlobalVariables() {
        return globalVariables;
    }

    public List<Variable> getCommonSymbols() {
        return commonSymbols;
    }

    private void initVariables () {
        globalVariables = new ArrayList<>();
        commonSymbols = new ArrayList<>();
        for (cat.footoredo.mx.entity.Variable variable: variables) {
            (variable.hasInitializer() ? globalVariables : commonSymbols).add (variable);
        }
    }
}
