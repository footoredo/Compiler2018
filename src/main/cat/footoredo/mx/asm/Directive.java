package cat.footoredo.mx.asm;

public class Directive extends Assembly {
    private String content;

    public Directive(String content) {
        this.content = content;
    }

    @Override
    public boolean isDirective() {
        return true;
    }

    @Override
    public String toSource(SymbolTable symbolTable) {
        return content;
    }
}
