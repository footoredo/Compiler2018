package cat.footoredo.mx.ir;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Location;

public class LabelStatement extends Statement {
    private Label label;

    public LabelStatement (Location location, Label label) {
        super (location);
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }
}
