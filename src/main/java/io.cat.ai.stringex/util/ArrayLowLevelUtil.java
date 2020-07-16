package io.cat.ai.stringex.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

public class ArrayLowLevelUtil {

    private ArrayLowLevelUtil() {}

    @SuppressWarnings("unchecked")
    public static <A> A[] mergeGenericArr(A[] left, A... right) {
        int leftLength = left.length;
        int rightLength = right.length;

        A[] result = (A[]) Array.newInstance(left.getClass().getComponentType(), leftLength + rightLength);

        System.arraycopy(left, 0, result, 0, leftLength);
        System.arraycopy(right, 0, result, leftLength, rightLength);
        return result;
    }

    public static char[] toPrimitiveCharArr(Collection<Character> characters) {
        Character[] array = (Character[]) characters.toArray();

        final char[] primitiveCharArr = new char[array.length];
        for (int i = 0; i < array.length; i++)
            primitiveCharArr[i] = array[i];

        return primitiveCharArr;
    }

    public static char[] mergeCharArr(char[] left, char... right) {
        int leftLength = left.length;
        int rightLength = right.length;

        char[] result = new char[leftLength + rightLength];
        System.arraycopy(left, 0, result, 0, leftLength);
        System.arraycopy(right, 0, result, leftLength, rightLength);
        return result;
    }

    public static char[] prependToCharArray(char[] ref, char value) {
        int refLength = ref.length;
        char[] newRef = new char[refLength + 1];

        System.arraycopy(ref, 0, newRef, 1, refLength);
        newRef[0] = value;

        return newRef;
    }

    public static char[] appendToCharArray(char[] ref, char value) {
        int refLength = ref.length;
        char[] newRef = Arrays.copyOf(ref, refLength + 1);
        newRef[ref.length] = value;
        return newRef;
    }

    @SuppressWarnings("unchecked")
    public static <A> A[] prependToArray(A[] ref, A value) {
        int refLength = ref.length;

        A[] newArrayRef = (A[]) Array.newInstance(ref.getClass().getComponentType(), refLength + 1);
        System.arraycopy(ref, 0, newArrayRef, 1, refLength);
        newArrayRef[0] = value;

        return newArrayRef;
    }

    public static char[] charArrTail(char[] array) {
        assert array != null;
        assert array.length != 0;
        return Arrays.copyOfRange(array, 1, array.length);
    }

}
