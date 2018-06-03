package cat.footoredo.mx.utils;

import java.util.HashSet;
import java.util.Set;

public class SetUtils {
    static public <T> Set<T> solveIntersection (Set <T> A, Set <T> B) {
        Set <T> tmp = new HashSet<>(A);
        tmp.retainAll(B);
        return tmp;
    }
}
