package cat.footoredo.mx.asm;

public class Comment extends Assembly {
    private String content;
    private int indentLevel;

    public Comment(String content, int indentLevel) {
        this.content = content;
        this.indentLevel = indentLevel;
    }

    public Comment(String content) {
        this (content, 0);
    }

    @Override
    public boolean isComment () {
        return true;
    }

    @Override
    public String toSource(SymbolTable symbolTable) {
        return "\t" + indent() + "\" " + content;
    }

    private String indent () {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < indentLevel; ++ i) {
            buffer.append("  ");
        }
        return buffer.toString();
    }
}
