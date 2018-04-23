package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class MemberType extends Type {
    private Map<String, Slot> memberMap;

    public MemberType(List<Slot> members) {
        super();
        this.memberMap = new HashMap<>();
        for (Slot slot: members) {
            // System.out.println(slot.getName() + ": " + slot.getTypeNode().toString());
            memberMap.put(slot.getName(), slot);
        }
    }
    public Type getMemberType(String member) {
        // System.out.println("querying " + member);
        return memberMap.get(member).getType();
    }
    public List<Slot> getMembers() {
        return new ArrayList<>(memberMap.values());
    }
}
