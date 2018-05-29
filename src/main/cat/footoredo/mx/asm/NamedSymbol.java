package cat.footoredo.mx.asm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NamedSymbol extends BaseSymbol {
    private String name;

    static final private String [] PRESERVED_NAMES = {
            "main", "print", "println", "getString", "getInt",
            "toString", "_strcmp", "_strcat", "malloc",
            "_Znam", "putchar", "puts", "scanf", "sprintf",
            "strcmp", "strcpy", "strlen", "strncpy",
            "string#length", "string#substring", "string#parseInt", "string#ord",
            "__array#size"
    };

    private static final Set<String> preserverNames = new HashSet<>(Arrays.asList(PRESERVED_NAMES));

    public NamedSymbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toSource() {
        if (preserverNames.contains(name))
            return name;
        else
            return "@_@" + name;
    }

    @Override
    public String toSource(SymbolTable symbolTable) {
        return toSource();
    }

    public String toString () {
        return "#" + name;
    }

    @Override
    public int cmp(IntegerLiteral i) {
        return 1;
    }

    @Override
    public int cmp(NamedSymbol symbol) {
        return name.compareTo(symbol.name);
    }

    @Override
    public int cmp(UnnamedSymbol symbol) {
        return -1;
    }

    @Override
    public int compareTo(Literal literal) {
        return -(literal.compareTo(this));
    }
}
