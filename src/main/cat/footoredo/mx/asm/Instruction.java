package cat.footoredo.mx.asm;

public class Instruction extends Assembly {
    private String mnemonic;
    private String suffix;
    private Operand[] operands;
    private boolean needRelocation;

    public Instruction(String mnemonic, String suffix, Operand[] operands, boolean needRelocation) {
        this.mnemonic = mnemonic;
        this.suffix = suffix;
        this.operands = operands;
        this.needRelocation = needRelocation;
        for (int i = 0; i < operands.length; ++ i) {
            if (operands[i] == null)
                throw new Error("fasd");
        }
    }

    public Instruction(String mnemonic, String suffix, Operand a1, Operand a2, boolean needRelocation) {
        this (mnemonic, suffix, new Operand[] {a1, a2}, needRelocation);
    }

    public Instruction(String mnemonic, String suffix, Operand a1, Operand a2) {
        this (mnemonic, suffix, a1, a2, false);
    }

    public Instruction(String mnemonic, String suffix, Operand a) {
        this (mnemonic, suffix, new Operand[] {a}, false);
    }

    public Instruction(String mnemonic, String suffix) {
        this (mnemonic, suffix, new Operand[0], false);
    }

    public Instruction(String mnemonic) {
        this (mnemonic, "", new Operand[0], false);
    }

    @Override
    public boolean isInstruction() {
        return true;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getSuffix() {
        return suffix;
    }

    public int numOperands() {
        return this.operands.length;
    }

    public Operand operand1() {
        return this.operands[0];
    }

    public Operand operand2() {
        return this.operands[1];
    }

    public boolean isJumpInstruction () {
        return mnemonic.equals("jmp") || mnemonic.equals("j");
    }

    @Override
    public void collectStatistics(Statistics statistics) {
        statistics.useInstruction(mnemonic + suffix);
        for (int i = 0; i < operands.length; ++ i) {
            // System.out.println(operands[i]);
            operands[i].collectStatistics (statistics);
        }
    }

    @Override
    public String toSource(SymbolTable symbolTable) {
        StringBuffer buf = new StringBuffer();
        buf.append("\t");
        buf.append(mnemonic).append(suffix);
        String sep = "\t";
        for (int i = 0; i < operands.length; i++) {
            buf.append(sep); sep = ", ";
            buf.append(operands[i].toSource(symbolTable));
        }
        return buf.toString();
    }
}
