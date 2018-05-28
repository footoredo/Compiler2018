package cat.footoredo.mx.entity;

import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.Type;

import java.util.*;

abstract public class Scope {
    private List <LocalScope> children;
    protected Map<String, Entity> entityMap;

    public Scope () {
        children = new ArrayList <> ();
        entityMap = new LinkedHashMap<>();
    }

    abstract public boolean isTopLevel ();
    abstract public ToplevelScope toplevel ();
    abstract public Scope parent ();

    public Variable allocateTmpVariable (Type type) {
        Variable tmp = Variable.tmp (type);
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
        if (entityMap.containsKey(entity.getName())) {
            throw new SemanticException(entity.getLocation(), "duplicated declaration of " + entity.getName());
        }
        entityMap.put(entity.name, entity);
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