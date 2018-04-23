package cat.footoredo.mx.type;

import cat.footoredo.mx.ast.Slot;

import java.util.List;

public class StringType extends MemberType {
    public StringType(List<Slot> members) {
        super(members);
    }

    public String toString () {
        return "string";
    }
}
