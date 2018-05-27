package cat.footoredo.mx.utils;

import java.util.Collections;
import java.util.List;

public class ListUtils {
    static public <T> List<T> reverse (List<T> list) {
        List<T> rList = list
        Collections.reverse(rList);
        return rList;
    }
}
