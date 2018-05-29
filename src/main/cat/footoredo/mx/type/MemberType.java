package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.MemberNode;
import cat.footoredo.mx.ast.Slot;
import cat.footoredo.mx.ir.Expression;

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
    public MemberType() {
        this.memberMap = new HashMap<>();
    }
    public void addMember(Slot slot) {
        memberMap.put(slot.getName(), slot);
    }
    public Type getMemberType(String member) {
        /*System.out.println("querying " + member);
        for (String key: memberMap.keySet()) {
            System.out.println(" --- " + key);
        }*/
        // System.out.println(memberMap.get(member).getTypeNode());

        try {
            //System.out.println (member + getName());
            return memberMap.get(member).getType();
        }catch (NullPointerException e) {
            /*for (String key: memberMap.keySet()) {
                System.out.println(" --- " + key);
            }*/
            throw e;
        }
    }
    public List<Slot> getMembers() {
        return new ArrayList<>(memberMap.values());
    }
    public Slot getMember (String name) {
        return memberMap.get(name);
    }
    public int getOffset (String name) {
        return getMember(name).getOffset();
    }
    public abstract String getName ();
}
