package cat.footoredo.mx.exception;

import cat.footoredo.mx.entity.Location;
import org.antlr.v4.runtime.*;

public class MxANTLRErrorStrategy extends BailErrorStrategy {
    private static Location getLocation (RecognitionException e) {
        Token offendingToken = e.getOffendingToken();
        return new Location(offendingToken);
    }

    @Override
    public void recover(Parser recognizer, RecognitionException e) throws ParsingException {
        Location location = getLocation(e);
        if (e instanceof FailedPredicateException)
            throw new ParsingException("FailedPredicateException occurred @ " + location.toString());
        else if (e instanceof InputMismatchException)
            throw new ParsingException("InputMismatchException occurred @ " + location.toString());
        else if (e instanceof LexerNoViableAltException)
            throw new ParsingException("LexerNoViableAltException occurred @ " + location.toString());
        else if (e instanceof NoViableAltException)
            throw new ParsingException("NoViableAltException occurred @ " + location.toString());
        else
            throw new ParsingException("wtf??");
    }
}
