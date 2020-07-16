package io.cat.ai.stringex;

public class StringExDsl {

    public static StringEx StringEx(String internal) {
        return new StringEx(internal);
    }

    public static StringEx StringEx(char... chars) {
        return new StringEx(new String(chars));
    }

    public static StringEx StringEx(StringEx stringEx) {
        return new StringEx(stringEx.internal());
    }
}
