package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.Function;

import java.util.List;

public class CallInst extends Instruction {
    private Function function;

    public CallInst (Operand result, Function function, List<Operand> operands) {
        super (result, operands);
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public int getArgc () {
        return getOperandsLength();
    }

    public Operand getArg (int i) {
        return getOperand(i);
    }

    @Override
    public void accept(CFGVisitor visitor) {
        visitor.visit(this);
    }
}
