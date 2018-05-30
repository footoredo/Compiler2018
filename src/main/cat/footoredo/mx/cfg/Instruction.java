package cat.footoredo.mx.cfg;

import java.util.List;

abstract public class Instruction {
    private Operand result;
    private List<Operand> operands;

    public Instruction(Operand result, List<Operand> operands) {
        this.result = result;
        this.operands = operands;
    }

    public Instruction(List<Operand> operands) {
        this (null, operands);
    }

    public int getOperandsLength() {
        return operands.size();
    }

    public List<Operand> getOperands() {
        return operands;
    }

    public Operand getOperand (int i) {
        return operands.get(i);
    }

    public Operand getResult() {
        return result;
    }

    abstract public void accept (CFGVisitor visitor);
}
