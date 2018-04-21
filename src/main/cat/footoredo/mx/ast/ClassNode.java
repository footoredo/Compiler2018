package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.ClassTypeRef;

import java.util.ArrayList;
import java.util.List;

public class ClassNode extends Node {
    protected Location location;
    private String name;
    private List<Variable> memberVariables;
    private List<DefinedFunction> memberMethods;
    private DefinedFunction constructor;
    private TypeNode typeNode;
    private List<Slot> members;
    private LocalScope scope;

    ClassNode(Location location, String name, ClassBodyNode classBodyNode) throws SemanticException {
        super ();
        this.location = location;
        this.name = name;
        this.memberVariables = new ArrayList<>();
        this.memberMethods = new ArrayList<>();
        this.constructor = null;
        this.members = new ArrayList<>();
        for (MemberDeclarationNode memberDeclarationNode : classBodyNode.getMemberDeclarationNodes()) {
            if (memberDeclarationNode instanceof MemberVariableDeclarationNode) {
                MemberVariableDeclarationNode memberVariableDeclarationNode = (MemberVariableDeclarationNode) memberDeclarationNode;
                memberVariables.add(new Variable(memberVariableDeclarationNode.getVariableDeclarationNode()));
                members.add(new Slot(memberVariableDeclarationNode.getTypeNode(), memberVariableDeclarationNode.getName()));
            }
            else if (memberDeclarationNode instanceof MemberMethodDeclarationNode) {
                MemberMethodDeclarationNode memberMethodDeclarationNode = (MemberMethodDeclarationNode) memberDeclarationNode;
                memberMethods.add(new DefinedFunction(memberMethodDeclarationNode.getMethodNode()));
                members.add(new Slot(memberMethodDeclarationNode.getMethodNode().getTypeNode(), memberMethodDeclarationNode.getMethodNode().getName()));
            }
            else if (memberDeclarationNode instanceof ConstructorDeclarationNode) {
                if (this.constructor != null) {
                    // System.out.println("sss");
                    throw new SemanticException(memberDeclarationNode.getLocation(), "Multiple constructors.");
                }
                ConstructorDeclarationNode constructorDeclarationNode = (ConstructorDeclarationNode) memberDeclarationNode;
                this.constructor = new DefinedFunction(constructorDeclarationNode.getConstructorNode());
                members.add(new Slot(constructorDeclarationNode.getConstructorNode().getTypeNode(), constructorDeclarationNode.getConstructorNode().getName()));
            }
        }
        this.typeNode = new TypeNode(new ClassTypeRef(location, name));

        /*System.out.println("A new class \"" + this.name + "\" is found @ " +
                this.location.toString() + " with " + Integer.toString(memberMethods.size()) + " methods and "
                + Integer.toString(memberVariables.size()) + " variables and " + (this.constructor == null ? "no " : "")
                + "constructor.");*/
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

    public List<DefinedFunction> getMemberMethods() {
        if (constructor == null)
            return memberMethods;
        else {
            List<DefinedFunction> ret = memberMethods;
            ret.add(constructor);
            return ret;
        }
    }

    public List<Entity> getEntitis() {
        List<Entity> ret = new ArrayList<>();
        ret.addAll(getMemberVariables());
        ret.addAll(getMemberMethods());
        return ret;
    }

    public DefinedFunction getConstructor() {
        return constructor;
    }

    public LocalScope getScope() {
        return scope;
    }

    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    public boolean hasConstructor() {
        return constructor != null;
    }
}
