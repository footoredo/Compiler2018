package cat.footoredo.mx.ir;

import cat.footoredo.mx.entity.*;

import java.util.ArrayList;
import java.util.List;

public class IR {
    private Location location;
    private List<cat.footoredo.mx.entity.Variable> variables;
    private List<DefinedFunction> definedFunctions;
    private List<BuiltinFunction> builtinFunctions;
    private ToplevelScope scope;

    public IR(Location location, List<cat.footoredo.mx.entity.Variable> variables, List<DefinedFunction> definedFunctions, List<BuiltinFunction> builtinFunctions, ToplevelScope scope) {
        this.location = location;
        this.variables = variables;
        this.definedFunctions = definedFunctions;
        this.builtinFunctions = builtinFunctions;
        this.scope = scope;
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
}
