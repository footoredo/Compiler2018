package cat.footoredo.mx.sysdep.x86_64;

import cat.footoredo.mx.asm.*;
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

    public AssemblyCode(Type naturalType, long stackWordSize, SymbolTable symbolTable) {
        this.naturalType = naturalType;
        this.stackWordSize = stackWordSize;
        this.symbolTable = symbolTable;
    }

    private Statistics getStatistics() {
        if (statistics == null) {
            statistics = Statistics.collect(assemblies);
        }
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
            IndirectMemoryReference memoryReference = relocatableMemoryReference(-offset, bp ());
            memoryReferences.add(memoryReference);
            return memoryReference;
        }

        private IndirectMemoryReference relocatableMemoryReference (long offset, Register base) {
            return IndirectMemoryReference.relocatable(offset, base);
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
        mov (virtualStack.top(), register);
    }

    public void virtualPop (Register register) {
        mov (register, virtualStack.top());
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
        instruction("db\t\"" + value + "\"", "");
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

    public void cwd () {
        instruction("c", "wd");
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

    public void mov (Register destination, Register source) {
        instruction("mov", destination, source);
    }

    public void mov(Operand destination, Register source) {
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
    }

    public void lea (Register destination, Operand source) {
        instruction("lea", destination, source);
    }

    public void neg (Register register) {
        instruction("neg", register);
    }

    public void add (Register base, Operand diff) {
        instruction("add", base, diff);
    }

    public void sub (Register base, Operand diff) {
        instruction("sub", base, diff);
    }

    public void imul (Register base, Operand diff) {
        instruction("imul", base, diff);
    }

    public void div (Register base) {
        instruction("div", base);
    }

    public void idiv (Register base) {
        instruction("idiv", base);
    }

    public void not (Register register) {
        instruction("not", register);
    }

    public void and (Register base, Operand bits) {
        instruction("and", base, bits);
    }

    public void or (Register base, Operand bits) {
        instruction("or", base, bits);
    }

    public void xor (Register base, Operand bits) {
        instruction("xor", base, bits);
    }

    public void sar (Register base, Register bits) {
        instruction("sar", base, bits);
    }

    public void sal (Register base, Register bits) {
        instruction("sal", base, bits);
    }

    public void shr (Register base, Register bits) {
        instruction("shr", base, bits);
    }

    public void shl (Register base, Register bits) {
        instruction("shl", base, bits);
    }

}
