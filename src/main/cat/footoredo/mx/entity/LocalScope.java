package cat.footoredo.mx.entity;

import cat.footoredo.mx.exception.SemanticException;

import java.util.LinkedHashMap;
import java.util.Map;

public class LocalScope extends Scope {
    private Scope parent;
    private Map<String, Variable> variableMap;

    public LocalScope (Scope parent) {
        super ();
        this.parent = parent;
        parent.addChild(this);
        variableMap = new LinkedHashMap<>();
    }

    @Override
    public boolean isTopLevel() {
        return false;
    }

    @Override
    public ToplevelScope toplevel() {
        return parent.toplevel();
    }

    @Override
    public Scope parent() {
        return null;
    }

    public void defineVariable(Variable var) throws SemanticException {
        if (variableMap.containsKey(var.getName())) {
            throw new SemanticException(var.getLocation(), "duplicated declaration of " + var.getName());
        }
        variableMap.put(var.getName(), var);
    }

    @Override
    public Entity get(String name) throws SemanticException {
        if (variableMap.containsKey(name)) {
            return variableMap.get(name);
        }
        else {
            return parent.get(name);
        }
    }
}
