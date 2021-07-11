package util;

public class ArrayUtils {

    public static boolean contains(int[] ns, int needle) {
        return indexOf(ns, needle) >= 0;
    }

    public static int indexOf(int[] ns, int needle) {
        int len = ns.length;
        for (int i = 0; i < len; i++) {
            if (ns[i] == needle) {
                return i;
            }
        }
        return -1;
    }

    public static int count(int[] ns, int needle) {
        int count = 0;
        for (int n : ns) {
            if (n == needle) {
                count++;
            }
        }
        return count;
    }
}
