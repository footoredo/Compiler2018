package cat.footoredo.mx.asm;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private String base;
    private Map<UnnamedSymbol, String> map;
    private long seq = 0;

    static private final String DUMMY_SYMBOL_BASE = "L";
    static private final SymbolTable dummy = new SymbolTable(DUMMY_SYMBOL_BASE);

    static public SymbolTable dummy() {
        return dummy;
    }

    public SymbolTable(String base) {
        this.base = base;
        this.map = new HashMap<>();
    }

    public Symbol newSymbol () {
        return new NamedSymbol(newString());
    }

    private String newString () {
        return base + (seq ++);
    }

    public String symbolString(UnnamedSymbol symbol) {
        if (map.containsKey(symbol)) {
            return map.get(symbol);
        }
        else {
            String newString = newString();
            map.put(symbol, newString);
            return newString;
        }
    }
}
