package cat.footoredo.mx.entity;

import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.Type;

import java.util.*;

abstract public class Scope {
    private List <LocalScope> children;
    Map<String, Entity> entityMap;

    public Scope () {
        children = new ArrayList <> ();
        entityMap = new LinkedHashMap<>();
    }

    abstract public boolean isTopLevel ();
    abstract public ToplevelScope toplevel ();
    abstract public Scope parent ();

    public Variable allocateTmpVariable (Type type) {
        Variable tmp = Variable.tmp (type);
        // System.out.println (tmp.getName() + " in " + this);
        declareEntity(tmp);
        return tmp;
    }

    public void declareEntity(Entity entity, Set<String> reservedWords) throws SemanticException {
        if (reservedWords.contains(entity.getName())) {
            throw new SemanticException(entity.getLocation(), "using reserverd word \"" + entity.getName() + "\"");
        }
        declareEntity(entity);
    }

    public void declareEntity(Entity entity) throws SemanticException {
        // System.out.println(entity.getName());
        if (entityMap.containsKey(entity.getName())) {
            throw new SemanticException(entity.getLocation(), "duplicated declaration of " + entity.getName());
        }
        entityMap.put(entity.name, entity);
    }

    public List<Function> getAllFunctions () {
        List<Function> result = new ArrayList<>();
        for (Entity entity: entityMap.values()) {
            if (entity instanceof Function) {
                result.add ((Function)entity);
            }
        }
        for (LocalScope scope: children) {
            result.addAll (scope.getAllFunctions());
        }
        return result;
    }

    public List<DefinedFunction> getDefinedFunctions () {
        List<DefinedFunction> result = new ArrayList<>();
        for (Entity entity: entityMap.values()) {
            if (entity instanceof DefinedFunction) {
                result.add ((DefinedFunction)entity);
            }
        }
        return result;
    }

    public List<DefinedFunction> getAllDefinedFunctions () {
        List<DefinedFunction> result = getDefinedFunctions();
        for (LocalScope scope: children) {
            result.addAll (scope.getAllDefinedFunctions());
        }
        return result;
    }

    public List<BuiltinFunction> getBuiltinFunctions () {
        List<BuiltinFunction> result = new ArrayList<>();
        for (Entity entity: entityMap.values()) {
            if (entity instanceof BuiltinFunction) {
                result.add ((BuiltinFunction)entity);
            }
        }
        return result;
    }

    public List<BuiltinFunction> getAllBuiltinFunctions () {
        List<BuiltinFunction> result = getBuiltinFunctions();
        for (LocalScope scope: children) {
            result.addAll (scope.getAllBuiltinFunctions());
        }
        return result;
    }

    public List<Variable> getVariables () {
        List<Variable> result = new ArrayList<>();
        for (Entity entity: entityMap.values()) {
            if (entity instanceof Variable) {
                result.add ((Variable)entity);
            }
        }
        return result;
    }

    public List<Variable> getAllVariables () {
        List<Variable> result = getVariables();
        for (LocalScope scope: children) {
            result.addAll(scope.getAllVariables());
        }
        return result;
    }

    public void addChild (LocalScope s) {
        children.add (s);
    }

    public List<LocalScope> getChildren() {
        return children;
    }

    abstract public Entity get (String name) throws SemanticException;

    public Entity directGet (String name) {
        return entityMap.getOrDefault(name, null);
    }
}