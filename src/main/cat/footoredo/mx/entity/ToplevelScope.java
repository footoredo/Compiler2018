package cat.footoredo.mx.entity;

import cat.footoredo.mx.exception.SemanticException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    private List<Variable> variables;
    private List<Variable> staticVariables;
    private List<Variable> nonstaticVariables;

    /*public List<Variable> getVariables () {
        if (variables == null) {
            variables = new ArrayList<>();
            for (Entity entity : super.entityMap.values()) {
                if (entity instanceof Variable) {
                    variables.add((Variable) entity);
                }
            }
        }
        return variables;
    }*/

    public List<Variable> getStaticVariables () {
        if (staticVariables == null) {
            cacheVariables ();
        }
        return staticVariables;
    }

    public List<Variable> getNonstaticVariables () {
        if (nonstaticVariables == null) {
            cacheVariables ();
        }
        return nonstaticVariables;
    }

    private void cacheVariables () {
        staticVariables = new ArrayList<>();
        nonstaticVariables = new ArrayList<>();
        for (Variable variable: getVariables()) {
            (variable.isStatic() ? staticVariables : nonstaticVariables).add (variable);
        }
    }
}
