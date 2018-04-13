package cat.footoredo.mx.entity;

import org.antlr.v4.runtime.Token;

public class Location {
    protected Token token;

    public Location (Token token) {
        this.token = token;
    }

    public String getSource() {
        return token.getTokenSource().getSourceName();
    }

    public Token getToken() {
        return token;
    }

    public int getLine () {
        return token.getLine();
    }

    public int getPositionInLine () {
        return token.getCharPositionInLine();
    }

    public String toString () {
        return getSource() + ":" + getLine () + "," + getPositionInLine();
    }
}
