package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticError;

import java.util.ArrayList;
import java.util.List;

public class ClassNode extends Node {
    protected Location location;
    private String name;
    private List<Variable> memberVariables;
    private List<Function> memberMethods;
    private Function constructor;

    public ClassNode(Location location, String name, ClassBodyNode classBodyNode) throws SemanticError {
        super ();
        this.location = location;
        this.name = name;
        this.memberVariables = new ArrayList<>();
        this.memberMethods = new ArrayList<>();
        this.constructor = null;
        for (MemberDeclarationNode memberDeclarationNode : classBodyNode.getMemberDeclarationNodes()) {
            if (memberDeclarationNode instanceof MemberVariableDeclarationNode)
                memberVariables.add (new Variable(((MemberVariableDeclarationNode) memberDeclarationNode).getVariableDeclarationNode()));
            else if (memberDeclarationNode instanceof MemberMethodDeclarationNode)
                memberMethods.add(new Function(((MemberMethodDeclarationNode) memberDeclarationNode).getMethodNode()));
            else if (memberDeclarationNode instanceof ConstructorDeclarationNode) {
                if (this.constructor != null) {
                    // System.out.println("sss");
                    throw new SemanticError(memberDeclarationNode.getLocation(), "Multiple constructors.");
                }
                this.constructor = new Function(((ConstructorDeclarationNode) memberDeclarationNode).getConstructorNode());
            }
        }

        System.out.println("A new class \"" + this.name + "\" is found @ " +
                this.location.toString() + " with " + Integer.toString(memberMethods.size()) + " methods and "
                + Integer.toString(memberVariables.size()) + " variables and " + (this.constructor == null ? "no " : "")
                + "constructor.");
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getMemberVariables() {
        return memberVariables;
    }

    public List<Function> getMemberMethods() {
        return memberMethods;
    }

    public Function getConstructor() {
        return constructor;
    }
}
