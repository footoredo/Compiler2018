package cat.footoredo.mx.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConstantTable implements Iterable<ConstantEntry> {
    private Map<String, ConstantEntry> table;

    public ConstantTable () {
        table = new LinkedHashMap<>();
    }

    public boolean isEmpty () {
        return table.isEmpty();
    }

    public ConstantEntry intern (String s) {
        if (table.containsKey(s)) {
            return table.get(s);
        }
        else {
            table.put (s, new ConstantEntry(s));
        }
    }

    public Collection<ConstantEntry> entries () {
        return table.values();
    }

    @Override
    public Iterator<ConstantEntry> iterator() {
        return table.values().iterator();
    }
}
