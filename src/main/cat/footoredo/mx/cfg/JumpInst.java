package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.Label;
import cat.footoredo.mx.entity.Variable;

import java.util.List;
import java.util.Set;

abstract public class JumpInst {
    abstract public void accept (CFGVisitor visitor);
    abstract public List<Label> getOutputs ();

    abstract public Set<Variable> backPropagate (Set<Variable> liveVariables);
}
