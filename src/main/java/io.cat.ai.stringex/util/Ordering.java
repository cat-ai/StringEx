package io.cat.ai.stringex.util;

public class Ordering {

    private Ordering() {}

    public static <A extends Comparable<A>> boolean isLess(A that, A comparable) {
        return that.compareTo(comparable) < 0;
    }

    public static <A extends Comparable<A>> boolean isGreater(A that, A comparable) {
        return that.compareTo(comparable) > 0;
    }

    public static <A extends Comparable<A>> boolean isEqual(A that, A comparable) {
        return that.compareTo(comparable) == 0;
    }
}
