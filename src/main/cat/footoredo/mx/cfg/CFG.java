package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;

import java.util.HashMap;
import java.util.Map;

public class CFG {
    private Map<Label, BasicBlock> labelTable;

    public CFG () {
        labelTable = new HashMap<>();
    }

    public void put (Label label, BasicBlock basicBlock) {
        if (basicBlock == null) {
            throw new Error("asdas");
        }
        // System.out.println ("putting " + label.hashCode());
        labelTable.put (label, basicBlock);
    }

    public BasicBlock get (Label label) {
        return labelTable.get (label);
    }
}
