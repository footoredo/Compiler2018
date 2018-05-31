package cat.footoredo.mx.asm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cat.footoredo.mx.sysdep.x86_64.Register;

public class Statistics {
    private Map<Register, Integer> registerUsage;
    private Map<String, Integer> instructionUsage;
    private Map<Symbol, Integer> symbolUsage;

    static public Statistics collect (List<Assembly> assemblies) {
        Statistics statistics = new Statistics();
        for (Assembly assembly: assemblies) {
            assembly.collectStatistics (statistics);
        }
        return statistics;
    }

    public Statistics() {
        registerUsage = new HashMap<>();
        instructionUsage = new HashMap<>();
        symbolUsage = new HashMap<>();
    }

    private <K> int fetchCount (Map<K, Integer> usage, K key) {
        /*if (key instanceof Register);
            System.out.println ("getting " + key.hashCode() + " " + usage.containsKey(key));*/
        return usage.getOrDefault(key, 0);
    }

    private <K> void incrementCount (Map<K, Integer> usage, K key) {
        /*if (key instanceof Register);
            System.out.println ("putting " + key.hashCode() + " " + (fetchCount(usage, key) + 1));*/
        usage.put (key, fetchCount(usage, key) + 1);
    }

    public boolean isRegisterUsed (Register register) {
        //System.out.println ("querying " + (register).getNumber() + " " + numRegisterUsed(register) + " " + register.hashCode());
        return numRegisterUsed(register) > 0;
    }

    public int numRegisterUsed (Register register) {
        return fetchCount(registerUsage, register);
    }

    public void useRegister (Register register) {
        //System.out.println ("adding " + (register).getNumber() + " " + register.hashCode());
        incrementCount (registerUsage, register);
    }

    public boolean isInstructionUsed (String instruction) {
        return numInstructionUsed(instruction) > 0;
    }

    public int numInstructionUsed (String instruction) {
        return fetchCount(instructionUsage, instruction);
    }

    public void useInstruction (String instruction) {
        incrementCount(instructionUsage, instruction);
    }

    public boolean isSymbolUsed (Symbol symbol) {
        return numSymbolUsed(symbol) > 0;
    }

    public int numSymbolUsed (Symbol symbol) {
        return fetchCount(symbolUsage, symbol);
    }

    public void useSymbol (Symbol symbol) {
        incrementCount(symbolUsage, symbol);
    }
}
