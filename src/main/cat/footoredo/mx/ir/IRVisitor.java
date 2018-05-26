package cat.footoredo.mx.ir;

public interface IRVisitor<S,E> {
    public S visit (ExpressionStatement s);
    public S visit (Assign s);
    public S visit (CJump s);
    public S visit (Jump s);
    public S visit (LabelStatement s);
    public S visit (Return s);
    public S visit (Null s);

    public E visit (Unary s);
    public E visit (Binary s);
    public E visit (Call s);
    public E visit (Address s);
    public E visit (Memory s);
    public E visit (Variable s);
    public E visit (Integer s);
    public E visit (String s);
    public E visit (Malloc s);
}
