package io.cat.ai.stringex.interop;

import java.util.*;

public class JavaCollectionInterop {

    private static JCollection DEFAULT_JCOLLECTION = JCollection.ARRAY_LIST;

    private JavaCollectionInterop() {}

    public static void applyJCollectionAsDefault(JCollection defaultJCollection) {
        DEFAULT_JCOLLECTION = defaultJCollection;
    }

    public enum JCollection {
        ARRAY_LIST, LINKED_LIST,

        QUEUE,

        STACK,

        TREE_SET, HASH_SET
    }

    private static <A> Collection<A> jCollection() {
        switch (DEFAULT_JCOLLECTION) {
            case LINKED_LIST:
                return new LinkedList<>();
            case QUEUE:
                return new ArrayDeque<>();
            case STACK:
                return new Stack<>();
            case TREE_SET:
                return new TreeSet<>();
            case HASH_SET:
                return new HashSet<>();

            case ARRAY_LIST:
            default:
                return new ArrayList<>();
        }
    }

    public static <A> Collection<A> defaultCollection() {
        return jCollection();
    }

    public static <A> Collection<A> newCollectionFromArray(A[] arr) {
        return Arrays.asList(arr);
    }
}