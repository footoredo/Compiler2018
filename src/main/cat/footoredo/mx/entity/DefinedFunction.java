package cat.footoredo.mx.entity;

import cat.footoredo.mx.asm.Symbol;
import cat.footoredo.mx.ast.BlockNode;
import cat.footoredo.mx.ast.MethodNode;
import cat.footoredo.mx.cfg.BasicBlock;
import cat.footoredo.mx.ir.Statement;

import java.util.List;

public class DefinedFunction extends Function {
    BlockNode block;
    LocalScope scope;
    List<Statement> IR;
    Symbol CFGSymbol;

    private BasicBlock startBasicBlock;
    private BasicBlock endBasicBlock;

    public DefinedFunction (MethodNode methodNode, String parentClass) {
        super (methodNode.getTypeNode(), methodNode.getMethodDescription(), parentClass);
        // System.out.println (parentClass + "#" + methodNode.getName());
        this.block = methodNode.getBlock();
    }

    public void appendFront (List<Statement> statements) {
        statements.addAll(IR);
        IR = statements;
    }

    public BasicBlock getStartBasicBlock() {
        return startBasicBlock;
    }

    public void setStartBasicBlock(BasicBlock startBasicBlock) {
        this.startBasicBlock = startBasicBlock;
    }

    public BasicBlock getEndBasicBlock() {
        return endBasicBlock;
    }

    public void setEndBasicBlock(BasicBlock endBasicBlock) {
        this.endBasicBlock = endBasicBlock;
    }

    public Symbol getCFGSymbol() {
        return CFGSymbol;
    }

    public void setCFGSymbol(Symbol CFGSymbol) {
        this.CFGSymbol = CFGSymbol;
    }

    public DefinedFunction (MethodNode methodNode) {
        this (methodNode, null);
    }

    public BlockNode getBlock() {
        return block;
    }

    public LocalScope getScope() {
        return scope;
    }

    public LocalScope getLvarScope () {
        return block.getScope();
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    public List<Statement> getIR() {
        return IR;
    }

    public void setIR(List<Statement> IR) {
        this.IR = IR;
    }

    @Override
    public <T> T accept(EntityVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
