package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;

import java.util.List;

abstract public class JumpInst {
    abstract public void accept (CFGVisitor visitor);
    abstract public List<Label> getOutputs ();
}
