package cat.footoredo.mx.entity;

import cat.footoredo.mx.exception.SemanticException;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToplevelScope extends Scope {

    public ToplevelScope () {
        super ();
    }

    @Override
    public boolean isTopLevel() {
        return true;
    }

    @Override
    public ToplevelScope toplevel() {
        return this;
    }

    @Override
    public Scope parent() {
        return null;
    }

    @Override
    public Entity get(String name) throws SemanticException {
        if (entityMap.containsKey(name)) {
            return entityMap.get(name);
        }
        else {
            throw new SemanticException("unresolved reference: " + name);
        }
    }
}
