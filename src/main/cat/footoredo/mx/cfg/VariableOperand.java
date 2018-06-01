package cat.footoredo.mx.cfg;

import cat.footoredo.mx.asm.MemoryReference;
import cat.footoredo.mx.asm.Register;
import cat.footoredo.mx.asm.Type;
import cat.footoredo.mx.entity.Scope;
import cat.footoredo.mx.entity.Variable;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.Map;

public class VariableOperand extends Operand {
    private Variable variable;

    public VariableOperand(Variable variable) {
        super (Type.get(variable.size()));
        this.variable = variable;
    }

    @Override
    public Operand copy() {
        return new VariableOperand(variable);
    }

    @Override
    public void replace(Map<Variable, Variable> replacement, Scope scope) {
        if (replacement.containsKey(variable)) {
            variable = replacement.get(variable);
        }
        else {
            Variable tmp = scope.allocateTmpVariable(variable.getType());
            replacement.put (variable, tmp);
            variable = tmp;
        }
    }

    @Override
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

    @Override
    public boolean isVariable() {
        return true;
    }
}
