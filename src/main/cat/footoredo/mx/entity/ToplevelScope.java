package cat.footoredo.mx.entity;

import cat.footoredo.mx.exception.SemanticError;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToplevelScope extends Scope {
    private Map<String, Entity> entityMap;

    public ToplevelScope () {
        super ();
        entityMap = new LinkedHashMap<>();
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

    public void declareEntity(Entity entity) throws SemanticError {
        if (entityMap.containsKey(entity.getName())) {
            throw new SemanticError(entity.getLocation(), "duplicated declaration of " + entity.getName());
        }
        entityMap.put(entity.name, entity);
    }

    @Override
    public Entity get(String name) throws SemanticError {
        if (entityMap.containsKey(name)) {
            return entityMap.get(name);
        }
        else {
            throw new SemanticError("unresolved reference: " + name);
        }
    }
}
