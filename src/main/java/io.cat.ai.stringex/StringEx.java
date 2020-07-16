package io.cat.ai.stringex;

import io.cat.ai.stringex.core.BaseStringEx;
import io.cat.ai.stringex.core.StringOps;
import io.cat.ai.stringex.util.Pair;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class StringEx extends AbstractStringEx  implements StringOps {

    private String internalStr;

    private Comparator<Character> characterComparator = Character::compareTo;

    StringEx(String internalStr) {
        super(internalStr.toCharArray());
        this.internalStr = internalStr;
    }
    private StringExIterator it = new StringExIterator(this);

    @Override
    public String internal() {
        return this.internalStr;
    }

    @Override
    public AbstractStringEx init(String str) {
        return new StringEx(str);
    }

    @Override
    public boolean exists(Predicate<Character> predicate) {
        AbstractStringEx cur = this;
        int cnt = 0;

        for(;cur.nonEmpty();) {
            char curHead = cur.head();
            cnt = predicate.test(curHead) ? cnt += 1 : cnt;
            cur = cur.tail();
        }
        return cnt != length();
    }

    @Override
    public Pair<AbstractStringEx, AbstractStringEx> partition(Predicate<Character> predicate) {
        AbstractStringEx cur = this;
        AbstractStringEx accL = init("");
        AbstractStringEx accR = init("");

        for (;cur.nonEmpty();) {
            char curHead = cur.head();
            if (predicate.test(curHead))
                accL = accL.append(curHead);
            else
                accR = accR.append(curHead);
            cur = cur.tail();
        }
        return Pair.of(accL, accR);
    }

    @Override
    public Pair<AbstractStringEx, AbstractStringEx> splitAt(int n) {
        return Pair.of(take(n), drop(n));
    }

    @Override
    public AbstractStringEx map(Function<Character, Character> f) {
        return super.mapOptimized(f);
    }

    @Override
    public <R> Collection<R> mapTo(Function<Character, R> f) {
        return super.mapToOptimized(f);
    }

    @Override
    public AbstractStringEx flatMap(Function<Character, BaseStringEx> f) {
        return super.flatMapOptimized(f);
    }

    @Override
    public <R> Collection<R> flatMapTo(Function<Character, Collection<R>> f) {
        return super.flatMapToOptimized(f);
    }

    @Override
    public AbstractStringEx sorted(Comparator<? super Character> c) {
        int len = length();
        AbstractStringEx acc = init("");

        if (len == 1)
            acc = acc.append(this);
        else if (len > 1) {
            Object[] array = new Object[len];
            int i = 0;

            AbstractStringEx cur = this;

            for (;cur.nonEmpty();) {
                array[i++] = cur.head();
                cur = cur.tail();
            }

            Arrays.sort(array, (Comparator<Object>) c);

            i = 0;

            for (;i < array.length;)
                acc = acc.append((Character) array[i++]);
        }

        return acc;
    }

    @Override
    public AbstractStringEx sorted() {
        return sorted(characterComparator);
    }

    @Override
    public <A extends Comparable<A>> BaseStringEx sortBy(Function<Character, A> f) {
        return sorted(Comparator.comparing(f));
    }

    @Override
    public AbstractStringEx sortBy(Comparator<? super Character> c) {
        return sorted(c);
    }

    @Override
    public BaseStringEx sortWith(Predicate<Pair<Character, Character>> pred) {
        return sorted(
                (left, right) ->
                        pred.test(Pair.of(left, right)) ? -1
                                : pred.test(Pair.of(right, left)) ? 1
                                    : 0
        );
    }

    @Override
    public Iterator<Character> iterator() {
        return it;
    }

    @Override
    public int codePointAt(int index) {
        return internal().codePointAt(index);
    }

    @Override
    public int codePointBefore(int index) {
        return internal().codePointBefore(index);
    }

    @Override
    public int codePointCount(int beginIndex, int endIndex) {
        return internal().codePointCount(beginIndex, endIndex);
    }

    @Override
    public int offsetByCodePoints(int index, int codePointOffset) {
        return internal().offsetByCodePoints(index, codePointOffset);
    }

    @Override
    public byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
        return internal().getBytes(charsetName);
    }

    @Override
    public byte[] getBytes(Charset charset) {
        return internal().getBytes(charset);
    }

    @Override
    public byte[] getBytes() {
        return internalStr.getBytes();
    }

    @Override
    public boolean contentEquals(StringBuffer sb) {
        return internal().contentEquals(sb);
    }

    @Override
    public boolean contentEquals(CharSequence cs) {
        return internal().contentEquals(cs);
    }

    @Override
    public boolean equalsIgnoreCase(String anotherString) {
        return internal().equalsIgnoreCase(anotherString);
    }

    @Override
    public int compareToIgnoreCase(String str) {
        return internal().compareToIgnoreCase(str);
    }

    @Override
    public boolean startsWith(String prefix, int toffset) {
        return internal().startsWith(prefix, toffset);
    }

    @Override
    public boolean startsWith(String prefix) {
        return internal().startsWith(prefix);
    }

    @Override
    public boolean endsWith(String suffix) {
        return internal().startsWith(suffix);
    }

    @Override
    public int indexOf(int ch) {
        return internal().indexOf(ch);
    }

    @Override
    public int indexOf(int ch, int fromIndex) {
        return internal().indexOf(ch, fromIndex);
    }

    @Override
    public int lastIndexOf(int ch) {
        return internal().lastIndexOf(ch);
    }

    @Override
    public int lastIndexOf(int ch, int fromIndex) {
        return internal().lastIndexOf(ch, fromIndex);
    }

    @Override
    public int indexOf(String str) {
        return internal().indexOf(str);
    }

    @Override
    public int indexOf(String str, int fromIndex) {
        return internal().indexOf(str, fromIndex);
    }

    @Override
    public int lastIndexOf(String str, int fromIndex) {
        return internal().lastIndexOf(str, fromIndex);
    }

    @Override
    public StringEx substring(int beginIndex) {
        return (StringEx) init(internalStr.substring(beginIndex));
    }

    @Override
    public StringEx substring(int beginIndex, int endIndex) {
        return (StringEx) init(internalStr.substring(beginIndex, endIndex));
    }

    @Override
    public StringEx replace(char oldChar, char newChar) {
        return (StringEx) init(internalStr.replace(oldChar, newChar));
    }

    @Override
    public boolean matches(String regex) {
        return internalStr.matches(regex);
    }

    @Override
    public StringEx replaceFirst(String regex, String replacement) {
        return (StringEx) init(internalStr.replaceFirst(regex, replacement));
    }

    @Override
    public StringEx replaceAll(String regex, String replacement) {
        return (StringEx) init(internalStr.replaceAll(regex, replacement));
    }

    @Override
    public StringEx replace(CharSequence target, CharSequence replacement) {
        return (StringEx) init(internalStr.replace(target, replacement));
    }

    @Override
    public StringOps trim() {
        return (StringEx) init(internalStr.trim());
    }

    private static class StringExIterator implements Iterator<Character> {

        private AbstractStringEx itSelf;

        StringExIterator(AbstractStringEx itSelf) {
            this.itSelf = itSelf;
        }

        @Override
        public boolean hasNext() {
            return !itSelf.isEmpty();
        }

        @Override
        public Character next() {
            if (itSelf.isEmpty()) {
                throw new NoSuchElementException("Empty");
            } else {
                char next = itSelf.head();
                itSelf = itSelf.tail();
                return next;
            }
        }
    }

    @Override
    public boolean asBoolean() throws RuntimeException {
        return Boolean.parseBoolean(internal());
    }

    @Override
    public byte asByte() throws RuntimeException {
        return Byte.parseByte(internal());
    }

    @Override
    public short asShort() throws RuntimeException {
        return Short.parseShort(internal());
    }

    @Override
    public float asFloat() throws RuntimeException {
        return Float.parseFloat(internal());
    }

    @Override
    public int asInt() throws RuntimeException {
        return Integer.parseInt(internal());
    }

    @Override
    public double asDouble() throws RuntimeException {
        return Double.parseDouble(internal());
    }

    @Override
    public long asLong() throws RuntimeException {
        return Long.parseLong(internal());
    }

    @Override
    public BigDecimal asBigDecimal() throws RuntimeException {
        return new BigDecimal(internal());
    }

    @Override
    public BigDecimal asBigDecimal(MathContext mc) throws RuntimeException {
        return new BigDecimal(internal(), mc);
    }

    @Override
    public BigInteger asBigInteger() throws RuntimeException {
        return new BigInteger(internal());
    }

    @Override
    public BigInteger asBigInteger(int radix) throws RuntimeException {
        return new BigInteger(internal(), radix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringEx stringEx = (StringEx) o;
        return Objects.equals(internalStr, stringEx.internalStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalStr);
    }
}