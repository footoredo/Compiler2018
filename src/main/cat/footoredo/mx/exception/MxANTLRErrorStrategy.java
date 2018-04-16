package cat.footoredo.mx.exception;

import cat.footoredo.mx.entity.Location;
import org.antlr.v4.runtime.*;

public class MxANTLRErrorStrategy extends BailErrorStrategy {
    private static Location getLocation (RecognitionException e) {
        Token offendingToken = e.getOffendingToken();
        return new Location(offendingToken);
    }

    @Override
    public void recover(Parser recognizer, RecognitionException e) throws ParsingError {
        Location location = getLocation(e);
        if (e instanceof FailedPredicateException)
            throw new ParsingError("FailedPredicateException occurred @ " + location.toString());
        else if (e instanceof InputMismatchException)
            throw new ParsingError("InputMismatchException occurred @ " + location.toString());
        else if (e instanceof LexerNoViableAltException)
            throw new ParsingError("LexerNoViableAltException occurred @ " + location.toString());
        else if (e instanceof NoViableAltException)
            throw new ParsingError("NoViableAltException occurred @ " + location.toString());
        else
            throw new ParsingError("wtf??");
    }
}
