package cat.footoredo.mx.entity;

import java.util.List;
import java.util.ArrayList;

abstract public class Scope {
    protected List <LocalScope> children;

    public Scope () {
        children = new ArrayList <> ();
    }

    abstract public boolean isTopLevel ();
    abstract public ToplevelScope toplevel ();
    abstract public Scope parent ();

    protected void addChild (LocalScope s) {
        children.add (s);
    }
}