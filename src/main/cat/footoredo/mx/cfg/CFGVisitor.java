package cat.footoredo.mx.cfg;

public interface CFGVisitor {
    public void visit (AssignInst inst);
    public void visit (CallInst inst);
    public void visit (BinaryInst inst);
    // public void visit (CompareInst inst);
    public void visit (UnaryInst inst);
    public void visit (DereferenceInst inst);
    public void visit (MallocInst inst);
    public void visit (ReturnInst inst);
    // public void visit (PushArgInst inst);
    public void visit (ConditionalJumpInst inst);
    public void visit (UnconditionalJumpInst inst);
    public void visit (ULTIMATERETURNINST inst);
}
