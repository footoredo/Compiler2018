package cat.footoredo.mx.entity;

import cat.footoredo.mx.OJ.Semantic;
import cat.footoredo.mx.exception.SemanticException;

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

    public void declareEntity(Entity entity, Set<String> reservedWords) throws SemanticException {
        if (reservedWords.contains(entity.getName())) {
            throw new SemanticException(entity.getLocation(), "using reserverd word \"" + entity.getName() + "\"");
        }
        if (entityMap.containsKey(entity.getName())) {
            throw new SemanticException(entity.getLocation(), "duplicated declaration of " + entity.getName());
        }
        entityMap.put(entity.name, entity);
    }

    public void addChild (LocalScope s) {
        children.add (s);
    }

    abstract public Entity get (String name) throws SemanticException;
}