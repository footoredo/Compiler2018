package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.BuiltinFunction;
import cat.footoredo.mx.entity.Entity;
import cat.footoredo.mx.entity.Function;
import cat.footoredo.mx.type.ProtoArrayType;
import cat.footoredo.mx.type.ProtoArrayTypeRef;
import cat.footoredo.mx.type.Type;

import java.util.ArrayList;
import java.util.List;

public class ProtoArrayTypeNode extends BuiltinTypeNode {
    private List<BuiltinFunction> memberMethods;
    private List<Slot> members;

    public ProtoArrayTypeNode(List<BuiltinFunction> memberMethods) {
        super(new ProtoArrayTypeRef());
        this.memberMethods = memberMethods;
        this.members = new ArrayList<>();
        for (BuiltinFunction function: memberMethods) {
            members.add(new Slot(function.getTypeNode(), function.getName()));
        }
    }

    public List<Function> getMemberMethods() {
        List<Function> ret = new ArrayList<>();
        ret.addAll(memberMethods);
        return ret;
    }

    public void addMemberMethods(BuiltinFunction function) {
        memberMethods.add(function);
    }

    public List<Entity> getEntities() {
        List<Entity> ret = new ArrayList<>();
        ret.addAll(memberMethods);
        return ret;
    }

    @Override
    public Type definingType() {
        return new ProtoArrayType(members);
    }
}
