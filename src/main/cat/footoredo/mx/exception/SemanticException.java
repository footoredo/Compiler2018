package cat.footoredo.mx.exception;

import cat.footoredo.mx.entity.Location;

public class SemanticException extends Error {
    protected Location location;
    public SemanticException(Location location, String msg) {
        super (location.toString() + ": " + msg);
    }
    public SemanticException(String msg) {
        super (msg);
    }
}
