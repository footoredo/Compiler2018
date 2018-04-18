package cat.footoredo.mx.entity;

import java.util.Map;

public class LocalScope extends Scope {
    private Scope parent;
    private Map<String, Variable> variableMap;

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
}
