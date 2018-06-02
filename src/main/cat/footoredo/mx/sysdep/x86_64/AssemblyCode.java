package cat.footoredo.mx.sysdep.x86_64;

import cat.footoredo.mx.asm.*;
import cat.footoredo.mx.ir.Integer;
import cat.footoredo.mx.ir.Op;

import java.util.ArrayList;
import java.util.List;

public class AssemblyCode implements cat.footoredo.mx.sysdep.AssemblyCode {
    private final Type naturalType;
    private final long stackWordSize;
    private final SymbolTable symbolTable;
    public final VirtualStack virtualStack = new VirtualStack();
    private int commentIndentLevel = 0;
    private List<Assembly> assemblies = new ArrayList<>();
    private Statistics statistics;

    static private boolean isInstant (Register register) {
        long index = register.getNumber();
        if (index == RegisterClass.AX.getValue() ||
                index == RegisterClass.CX.getValue() ||
                index == RegisterClass.DX.getValue()) {
            return true;
        }
        else {
            return false;
        }
    }

    public AssemblyCode(Type naturalType, long stackWordSize, SymbolTable symbolTable) {
        this.naturalType = naturalType;
        this.stackWordSize = stackWordSize;
        this.symbolTable = symbolTable;
    }

    public AssemblyCode peepholeOptimize () {
        int n = assemblies.size();
        List <Assembly> newAssemblies = new ArrayList<>();

        for (int i = 0; i < n - 1; ++ i) {
            if (assemblies.get(i) instanceof Instruction
                    && assemblies.get(i + 1) instanceof Instruction) {
                Instruction IA = (Instruction) (assemblies.get(i)),
                        IB = (Instruction) (assemblies.get(i + 1));
                if (IA.isMOV() && IB.isMOV() &&
                        IA.operand1().hashCode() == IB.operand2().hashCode() &&
                        IA.operand2().hashCode() == IB.operand1().hashCode()) {
                    newAssemblies.add (assemblies.get(i));
                    i += 2;
                    continue;
                }
                else if (IA.isMOV() && IB.isMOV() &&
                        IA.operand2().hashCode() == IB.operand1().hashCode() &&
                        IA.operand1().isRegister() &&
                        isInstant((Register) IA.operand1()) &&
                        ! (IA.operand1().isMemoryReference() && IA.operand2().isMemoryReference()) &&
                        ((Register)IA.operand2()).getType() == ((Register)IB.operand1()).getType()) {
                    newAssemblies.add (new Instruction("mov", "", IA.operand1(), IB.operand2()));
                    i += 2;
                    continue;
                }
            }
            newAssemblies.add (assemblies.get(i));
        }

        assemblies = newAssemblies;
        return this;
    }

    private Statistics getStatistics() {
        statistics = Statistics.collect(assemblies);
        return statistics;
    }

    public boolean used (Register register) {
        return getStatistics().isRegisterUsed(register);
    }

    public VirtualStack getVirtualStack() {
        return virtualStack;
    }

    public List<Assembly> getAssemblies() {
        return assemblies;
    }

    public void addAll(List<Assembly> assemblies) {
        this.assemblies.addAll(assemblies);
    }

    public String toSource () {
        StringBuffer buffer = new StringBuffer();
        for (Assembly assembly: assemblies) {
            buffer.append(assembly.toSource(symbolTable));
            buffer.append('\n');
        }
        return buffer.toString();
    }

    public void comment(String content) {
        assemblies.add(new Comment(content, commentIndentLevel));
    }

    public void indentComment() {
        commentIndentLevel++;
    }

    public void unindentComment() {
        commentIndentLevel--;
    }

    public void label(Symbol symbol) {
        assemblies.add(new Label(symbol));
    }

    public void label(Label label) {
        assemblies.add(label);
    }

    private void directive(String directive) {
        assemblies.add(new Directive(directive));
    }

    private void instruction (String op, String suffix) {
        assemblies.add (new Instruction(op, suffix));
    }

    private void instruction (String op, Operand a) {
        instruction(op,"", a);
    }

    private void instruction (String op, String suffix, Operand a) {
        assemblies.add (new Instruction(op, suffix, a));
    }

    private void instruction (String op, Operand a1, Operand a2) {
        assemblies.add (new Instruction(op, "", a1, a2));
    }

    public void _global (String name) {
        directive("\tglobal\t" + name);
    }

    public void _section (String name) {
        directive("\tsection\t" + name);
    }

    public void _text () {
        _section(".text");
    }

    public void _bss () {
        _section(".bss");
    }

    public void _data () {
        _section(".data");
    }

    public void _rodata () {
        _section(".rodata");
    }

    public void _extern (String name) {
        directive("\textern\t" + name);
    }

    class VirtualStack {
        private long offset;
        private long maxSize;
        private List<IndirectMemoryReference> memoryReferences = new ArrayList<>();

        public VirtualStack() {
            reset ();
        }

        public void reset () {
            offset = 0;
            maxSize = 0;
            memoryReferences.clear();
        }

        public long getMaxSize () {
            return maxSize;
        }

        public void extend (long length) {
            offset += length;
            if (offset > maxSize) maxSize = offset;
        }

        public void rewind (long length) {
            offset -= length;
        }

        public IndirectMemoryReference top () {
            IndirectMemoryReference memoryReference = relocatableMemoryReference(naturalType, -offset, bp ());
            memoryReferences.add(memoryReference);
            return memoryReference;
        }

        private IndirectMemoryReference relocatableMemoryReference (Type type, long offset, Register base) {
            return IndirectMemoryReference.relocatable(type, offset, base);
        }

        private Register bp () {
            return new Register(5, naturalType);
        }

        public void fixOffset (long diff) {
            for (IndirectMemoryReference memoryReference: memoryReferences) {
                memoryReference.fixOffset(diff);
            }
        }
    }

    public void virtualPush (Register register) {
        virtualStack.extend(stackWordSize);
        mov (virtualStack.top(), register.forType(naturalType));
    }

    public void virtualPop (Register register) {
        mov (register.forType(naturalType), virtualStack.top());
        virtualStack.rewind (stackWordSize);
    }

    private String getSuffix (long size) {
        switch ((int)size) {
            case 1: return "b";
            case 2: return "w";
            case 4: return "d";
            case 8: return "q";
            default:
                throw new Error("unsupported size");
        }
    }

    public void d (long size, long value) {
        instruction("d", getSuffix(size), new ImmediateValue(value));
    }

    public void d (String value) {
        instruction("db\t" + value, "");
    }

    public void res (long size) {
        instruction("res", getSuffix(size), new ImmediateValue(1));
    }

    public void jmp (Label label) {
        instruction("jmp", new ImmediateValue(label.getSymbol()));
    }

    public void j (String suffix, Label label) {
        instruction("j", suffix, new ImmediateValue(label.getSymbol()));
    }

    public void cmp (Operand a, Operand b) {
        instruction("cmp", a, b);
    }

    public void c (Type t) {
        String suffix;
        switch (t) {
            case INT16: suffix = "wd"; break;
            case INT32: suffix = "dq"; break;
            case INT64: suffix = "qo"; break;
            default:
                throw new Error ("unsupported c");
        }
        instruction("c", suffix);
    }

    public void set (String suffix, Register register) {
        instruction("set", suffix, register);
    }

    public void test (Register a, Register b) {
        instruction("test", a, b);
    }

    public void push (Register register) {
        instruction("push", register);
    }

    public void pop (Register register) {
        instruction("pop", register);
    }

    public void call (Symbol symbol) {
        instruction("call", new ImmediateValue(symbol));
    }

    public void leave () {
        instruction("leave", "");
    }

    public void ret() {
        instruction("ret", "");
    }

    void relocatableMov(Operand dest, Operand src) {
        assemblies.add(new Instruction("mov", "", dest, src, true));
    }

    public void mov (Operand destination, Operand source) {
        if (destination.hashCode() != source.hashCode())
            instruction("mov", destination, source);
    }

    public void movzx (Register destination, Operand source) {
        instruction("movzx", destination, source);
    }

    /*public void mov(Operand destination, Register source) {
        instruction("mov", destination, source);
    }

    public void mov (Operand destination, ImmediateValue source) {
        instruction("mov", destination, source);
    }

    public void mov(Register destination, Operand source) {
        instruction("mov", destination, source);
    }

    public void movzx (Register destination, Register source) {
        instruction("movzx", destination, source);
    }

    public void movzx (Operand destination, Register source) {
        instruction("movzx", destination, source);
    }

    public void movzx (Register destination, Operand source) {
        instruction("movzx", destination, source);
    }*/

    public ImmediateValue integer (long value) {
        return new ImmediateValue(value);
    }

    public void lea (Register destination, Operand source) {
        instruction("lea", destination, source);
    }

    public void neg (Operand operand) {
        instruction("neg", operand);
    }

    public void add (Operand base, Operand diff) {
        if (diff.isConstantInteger() && ((ImmediateValue)diff).getIntegerValue() == 0)
            return;
        instruction("add", base, diff);
    }

    public void sub (Operand base, Operand diff) {
        if (diff.isConstantInteger() && ((ImmediateValue)diff).getIntegerValue() == 0)
            return;
        instruction("sub", base, diff);
    }

    private boolean isPowerOfTwo (long number) {
        return number > 0 && ((number & (number - 1)) == 0);
    }

    public void imul (Operand base, Operand diff) {
        if (diff.isConstantInteger()) {
            long value = ((ImmediateValue)diff).getIntegerValue();
            if (value == 0) {
                mov (base, integer(0));
                return;
            }
            else if (value == 1) {
                return;
            }
            else if (isPowerOfTwo (value)) {
                long logValue = 0;
                while (value > 1) {
                    logValue ++;
                    value /= 2;
                }
                instruction("sal", base, integer(logValue));
                return;
            }
        }
        instruction("imul", base, diff);
    }

    public void div (Operand base) {
        instruction("div", base);
    }

    public void idiv (Operand base) {
        instruction("idiv", base);
    }

    public void not (Operand operand) {
        instruction("not", operand);
    }

    public void and (Operand base, Operand bits) {
        instruction("and", base, bits);
    }

    public void or (Operand base, Operand bits) {
        if (bits.isConstantInteger() && ((ImmediateValue)bits).getIntegerValue() == 0)
            return;
        instruction("or", base, bits);
    }

    public void xor (Operand base, Operand bits) {
        if (bits.isConstantInteger() && ((ImmediateValue)bits).getIntegerValue() == 0)
            return;
        instruction("xor", base, bits);
    }

    public void sar (Operand base, Register bits) {
        instruction("sar", base, bits);
    }

    public void sal (Operand base, Register bits) {
        instruction("sal", base, bits);
    }

    public void shr (Operand base, Register bits) {
        instruction("shr", base, bits);
    }

    public void shl (Operand base, Register bits) {
        instruction("shl", base, bits);
    }

}
