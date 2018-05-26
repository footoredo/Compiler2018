package cat.footoredo.mx.sysdep.x86_64;

import cat.footoredo.mx.asm.*;
import cat.footoredo.mx.entity.ConstantEntry;
import cat.footoredo.mx.entity.Entity;
import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.entity.Variable;
import cat.footoredo.mx.ir.IR;
import cat.footoredo.mx.ir.IRVisitor;

import java.util.List;

public class CodeGenerator implements cat.footoredo.mx.sysdep.CodeGenerator, IRVisitor<Void, Void> {
    private final Type naturalType;

    public CodeGenerator (Type naturalType) {
        this.naturalType = naturalType;
    }

    public AssemblyCode generate (IR ir) {
        locateSymbols(ir);
        return generateAssemblyCode(ir);
    }

    private static final String LABEL_SYMBOL_BASE = ".L";
    private static final String CONST_SYMBOL_BASE = ".LC";

    private void locateSymbols (IR ir) {
        SymbolTable constSymbols = new SymbolTable(CONST_SYMBOL_BASE);
        for (ConstantEntry entry: ir.getConstantTable().entries()) {
            locateStringLiteral (entry, constSymbols);
        }
        for (Variable variable: ir.getVariables()) {
            locateVariable (variable);
        }
        for (Function function: ir.getAllFunctions()) {
            locateFunction (function);
        }
    }

    private void locateStringLiteral(ConstantEntry entry, SymbolTable symbolTable) {
        entry.setSymbol(symbolTable.newSymbol());
        entry.setMemoryReference(memory(entry.getSymbol()));
        entry.setAddress(immediate(entry.getSymbol()));
    }

    private void locateVariable (Entity entity) {
        Symbol sym = symbol (entity.getSymbolString());
        entity.setMemoryReference(memory(sym));
        entity.setAddress(immediate(sym));
    }

    private void locateFunction (Function function) {
        function.setCallingSymbol(symbol (function.getSymbolString()));
        locateVariable(function);
    }

    private Symbol symbol(String sym) {
        return new NamedSymbol(sym);
    }

    private AssemblyCode generateAssemblyCode(IR ir) {
        AssemblyCode file = newAssemblyCode();
        generateDataSection (file, ir.getGlobalVariables());
        generateReadOnlyDataSection (file, ir.getConstantTable());
        generateTextSection (file, ir.getDefinedFunctions());
        generateCommonSymbols (file, ir.getCommonSymbols());
        return file;
    }

    private AssemblyCode newAssemblyCode() {
        return new AssemblyCode(naturalType, STACK_WORD_SIZE, new SymbolTable(LABEL_SYMBOL_BASE));
    }

    private void generateDataSection(AssemblyCode file, List<Variable> globalVariables) {
        file._data();
        for (Variable variable: globalVariables) {
            Symbol sym = symbol(variable.getSymbolString());

        }
    }

    static final private long STACK_WORD_SIZE = 8;
}
