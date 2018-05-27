package cat.footoredo.mx.sysdep.x86_64;

public enum RegisterClass {
    AX(0), CX(1), DX(2), BX(3), SP(4), BP(5), SI(6), DI(7);

    private int value;

    RegisterClass (int value) {
        this.value = value;
    }

    public int getValue () {
        return value;
    }
}
