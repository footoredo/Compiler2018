package cat.footoredo.mx.asm;

abstract public class BaseSymbol implements Symbol {
    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public Literal plus(long diff) {
        throw new Error("must not happen: BaseSymbol.plus called");
    }
}
