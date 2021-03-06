package cat.footoredo.mx.ast;

import java.util.ArrayList;
import java.util.List;

public class ClassBodyNode extends Node {
    private List<MemberDeclarationNode> memberDeclarationNodes;
    public ClassBodyNode () {
        super ();
        this.memberDeclarationNodes = new ArrayList<>();
    }

    public void addMemberDeclarationNode (MemberDeclarationNode memberDeclarationNode) {
        this.memberDeclarationNodes.add (memberDeclarationNode);
    }

    @Override
    public boolean isMemorable() {
        throw new Error ("querying ClassBodyNode#isMemorable");
    }

    public List<MemberDeclarationNode> getMemberDeclarationNodes() {
        return memberDeclarationNodes;
    }
}
