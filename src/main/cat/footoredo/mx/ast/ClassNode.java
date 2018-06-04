package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.*;
import cat.footoredo.mx.exception.SemanticException;
import cat.footoredo.mx.type.ClassType;
import cat.footoredo.mx.type.ClassTypeRef;
import cat.footoredo.mx.type.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassNode extends TypeDefinition {
    protected Location location;
    private List<Variable> memberVariables;
    private Set<String> memberVariableNames;
    private List<DefinedFunction> memberMethods;
    private DefinedFunction constructor;
    private List<Slot> members;
    private String name;
    ClassNode(Location location, String name, ClassBodyNode classBodyNode) throws SemanticException {
        super (new TypeNode(new ClassTypeRef(location, name)));
        this.location = location;
        this.name = name;
        this.memberVariables = new ArrayList<>();
        this.memberMethods = new ArrayList<>();
        this.memberVariableNames = new HashSet<>();
        this.constructor = null;
        this.members = new ArrayList<>();
        for (MemberDeclarationNode memberDeclarationNode : classBodyNode.getMemberDeclarationNodes()) {
            if (memberDeclarationNode instanceof MemberVariableDeclarationNode) {
                MemberVariableDeclarationNode memberVariableDeclarationNode = (MemberVariableDeclarationNode) memberDeclarationNode;
                memberVariables.add(new Variable(memberVariableDeclarationNode.getVariableDeclarationNode()));
                members.add(new Slot(memberVariableDeclarationNode.getTypeNode(), memberVariableDeclarationNode.getName()));
                memberVariableNames.add (memberVariableDeclarationNode.getName());
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
            else if (memberDeclarationNode instanceof MemberMethodDeclarationNode) {
                MemberMethodDeclarationNode memberMethodDeclarationNode = (MemberMethodDeclarationNode) memberDeclarationNode;
                memberMethods.add(new DefinedFunction(memberMethodDeclarationNode.getMethodNode(), name));
                members.add(new Slot(memberMethodDeclarationNode.getMethodNode().getTypeNode(), memberMethodDeclarationNode.getMethodNode().getName()));
            }
        }

        if (this.constructor == null) {
            BlockNode body = new BlockNode(null);
            ConstructorNode constructorNode = new ConstructorNode(null, this.name, new ArrayList<>(), body);
            this.constructor = new DefinedFunction(constructorNode);
            members.add (new Slot (constructorNode.getTypeNode(), this.name));
        }

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

    public List<Function> getMemberMethods() {
        List<Function> ret = new ArrayList<>();
        ret.addAll(memberMethods);
        if (constructor != null) {
            ret.add(constructor);
        }
        return ret;
    }

    public boolean hasMemberVariable (String name) {
        return memberVariableNames.contains(name);
    }

    public List<Entity> getEntities() {
        List<Entity> ret = new ArrayList<>();
        ret.addAll(getMemberVariables());
        ret.addAll(getMemberMethods());
        return ret;
    }

    public DefinedFunction getConstructor() {
        return constructor;
    }

    public boolean hasConstructor() {
        return constructor != null;
    }

    public ClassType getClassType () {
        // System.out.println("asdasd" + getT);
        if (getType() == null) super.getTypeNode().setType(definingType());
        return (ClassType) getType();
    }

    @Override
    public Type definingType() {
        return new ClassType(location, name, members);
    }

    @Override
    public <T> T accept(TypeDefinitionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
