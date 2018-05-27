package cat.footoredo.mx.entity;

import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.Type;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LocalScope extends Scope {
    private Scope parent;

    public LocalScope (Scope parent) {
        super ();
        this.parent = parent;
        parent.addChild(this);
    }

    public Variable allocateTmpVariable (Type type) {
        Variable tmp = Variable.tmp (type);
        declareEntity(tmp);
        return tmp;
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

    @Override
    public Entity get(String name) throws SemanticException {
        if (entityMap.containsKey(name)) {
            return entityMap.get(name);
        }
        else {
            return parent.get(name);
        }
    }

    public List<Variable> getLocalVariables () {
        List<Variable> result = new ArrayList<>();
        for (Entity entity: super.entityMap.values()) {
            if (entity instanceof Variable) {
                result.add ((Variable)entity);
            }
        }
        return result;
    }

    public List<Variable> getAllLocalVariables () {
        List<Variable> result = getLocalVariables();
        for (LocalScope scope: getChildren()) {
            result.addAll (scope.getAllLocalVariables());
        }
        return result;
    }

}
