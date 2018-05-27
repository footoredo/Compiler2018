package cat.footoredo.mx.utils;

public class AsmUtils {
    private AsmUtils() {}

    public static int align (int offset, int size) {
        return (offset + size - 1) / size * size;
    }
    public static long align (long offset, long size) {
        return (offset + size - 1) / size * size;
    }
}
