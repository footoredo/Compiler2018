package cat.footoredo.mx.utils;

public class StringUtils {
    public static String unescape (String raw) {
        StringBuffer buffer = new StringBuffer();
        int length = raw.length();
        for (int i = 1; i < length - 1; ++ i) {
            char now = raw.charAt(i);
            if (now == '\\') {
                ++ i;
                buffer.append (unescape (raw.charAt(i)));
            }
            else {
                buffer.append(now);
            }
        }
        return buffer.toString();
    }

    private static char unescape (char ch) {
        switch (ch) {
            case '\'':
            case '\"':
            case '\\': return ch;
            case 'b':  return '\b';
            case 'f':  return '\f';
            case 'n':  return '\n';
            case 't':  return '\t';
            default:   throw new Error("illegal escape: " + "\\" + ch);
        }
    }

    public static String escape (String raw) {
        StringBuffer buffer = new StringBuffer();
        int length = raw.length();
        boolean isFirst = true;
        for (int i = 0; i < length; ++ i) {
            if (!isFirst) {
                buffer.append(", ");
            }
            isFirst = false;
            buffer.append(String.format ("%02XH", (int)(raw.charAt(i))));
        }
        if (!isFirst) {
            buffer.append(", ");
        }
        buffer.append("00H");
        return buffer.toString();
    }
}
