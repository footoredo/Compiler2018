package cat.footoredo.mx.cfg;

import cat.footoredo.mx.entity.ToplevelScope;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.sysdep.x86_64.RegisterClass;

import java.util.*;

public class RegisterAllocator {
    private static final long [] AVAILABLE_REGISTERS = {
            12, 13, 14, 15, RegisterClass.BX.getValue(),
            RegisterClass.SI.getValue(), RegisterClass.DI.getValue(),
            8, 9, 10, 11
    };
    private static final int NUMBER_REGISTERS = /*AVAILABLE_REGISTERS.length*/ 2;

    private LinkedList<Variable> coloringStack;
    private Set<Variable> remainingVariables;

    static public void solveRivalry (Set<Variable> liveVariables) {
        for (Variable a: liveVariables)
            for (Variable b: liveVariables)
                a.addRivalry(b);
    }

    public void solve (ToplevelScope scope) {
        coloringStack = new LinkedList<>();
        remainingVariables = new HashSet<>();
        collect(scope);

        while (!remainingVariables.isEmpty()) {
            boolean found = false;
            Variable bestVariable = null;
            int mostUsedCount = -1;
            for (Variable variable: remainingVariables) {
                if (variable.getRivalryCount() < NUMBER_REGISTERS) {
                    found = true;
                    bestVariable = variable;
                    break;
                }
                else if (variable.getUsedCount() > mostUsedCount) {
                    bestVariable = variable;
                    mostUsedCount = variable.getUsedCount();
                }
            }

            if (found) {
                coloringStack.add (bestVariable);
            }

            bestVariable.disconnect ();
            remainingVariables.remove(bestVariable);
        }

        while (!coloringStack.isEmpty()) {
            Variable variable = coloringStack.getLast();
            coloringStack.removeLast();

            Set<Long> remainingRegister = fillRegisters ();
            for (Variable rivalry: variable.getOriginalRivalries()) {
                if (rivalry.isRegister()) {
                    remainingRegister.remove (rivalry.getRegister().getNumber());
                }
            }

            variable.setRegister(remainingRegister.iterator().next());
        }
    }

    private Set<Long>  fillRegisters () {
        Set<Long> registers = new TreeSet<>();
        for (int i = 0; i < NUMBER_REGISTERS; ++ i)
            registers.add (AVAILABLE_REGISTERS[i]);
        return registers;
    }

    private void collect (ToplevelScope scope) {
        for (Variable variable: scope.getAllVariables())
            if (!variable.isGlobal() && variable.isUsed()) {
                remainingVariables.add (variable);
            }
    }
}
