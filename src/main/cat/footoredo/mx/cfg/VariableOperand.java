package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Register;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.Variable;
import jdk.nashorn.internal.ir.annotations.Ignore;

public class VariableOperand extends Operand {
    private Variable variable;

    public VariableOperand(Variable variable) {
        super (Type.get(variable.size()));
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }

    public String getName () {
        return variable.getName();
    }

    @Override
    public boolean isRegister () {
        return variable.isRegister ();
    }

    @Override
    public boolean isMemory() {
        return variable.isMemory();
    }

    @Override
    public MemoryReference getMemoryReference() {
        return variable.getMemory();
    }

    @Override
    public Register getRegister() {
        return variable.getRegister();
    }
}
