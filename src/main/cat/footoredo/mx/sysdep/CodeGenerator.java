package cat.footoredo.mx.sysdep;

import cat.footoredo.mx.ir.IR;

public interface CodeGenerator {
    AssemblyCode generate (IR ir);
}
