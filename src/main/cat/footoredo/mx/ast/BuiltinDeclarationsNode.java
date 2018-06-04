package cat.footoredo.mx.ast;

import cat.footoredo.mx.entity.BuiltinFunction;
import cat.footoredo.mx.entity.Function;

import java.util.List;

public class BuiltinDeclarationsNode extends Node {
    private Declarations declarations;

    public BuiltinDeclarationsNode(Declarations declarations) {
        super ();
        this.declarations = declarations;
    }

    @Override
    public boolean isMemorable() {
        throw new Error ("querying BuiltinDeclarationsNode#isMemorable");
    }

    public void addFun(BuiltinFunction fun) {
        declarations.addFun(fun);
    }

    public List<BuiltinFunction> getFunctions() { return declarations.getBuiltinFuns(); }
}
