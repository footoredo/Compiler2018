package cat.footoredo.mx.exception;

import cat.footoredo.mx.entity.Location;

public class SemanticError extends Error {
    protected Location location;
    public SemanticError (Location location, String msg) {
        super (location.toString() + ": " + msg);
    }
    public SemanticError (String msg) {
        super (msg);
    }
}
