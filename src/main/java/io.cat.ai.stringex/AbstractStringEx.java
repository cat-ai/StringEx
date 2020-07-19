package io.cat.ai.stringex;

import io.cat.ai.stringex.core.BaseStringEx;
import io.cat.ai.stringex.core.CharSequenceEx;
import io.cat.ai.stringex.interop.JavaCollectionInterop;
import io.cat.ai.stringex.util.Ordering;
import io.cat.ai.stringex.util.Pair;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.cat.ai.stringex.interop.JavaCollectionInterop.*;
import static io.cat.ai.stringex.util.ArrayLowLevelUtil.*;
import static io.cat.ai.stringex.util.Ordering.*;

public abstract class AbstractStringEx implements BaseStringEx {

    protected final char[] arr;

    protected AbstractStringEx(char[] arr) {
        this.arr = arr;
    }

    @Override
    public char last() {
        if (isEmpty()) throw new NoSuchElementException("");
        AbstractStringEx cur = this;

        for (;;) {
            if (cur.tail().isEmpty())
                return cur.head();

            cur = cur.tail();
        }
    }

    @Override
    public AbstractStringEx reverse() {
        AbstractStringEx cur = this;
        AbstractStringEx acc = init("");

        for (;;) {
            if (cur.isEmpty()) {
                return acc;
            } else {
                acc = acc.prepend(cur.head());
                cur = cur.tail();
            }
        }
    }

    public char[] toCharArray() {
        return internal().toCharArray();
    }

    public abstract AbstractStringEx init(String str);

    public abstract Pair<AbstractStringEx, AbstractStringEx> partition(Predicate<Character> predicate);

    public abstract boolean exists(Predicate<Character> predicate);

    @Override
    public char head() {
        return arr[0];
    }

    @Override
    public Optional<Character> headOpt() {
        return Optional.of(head());
    }

    @Override
    public Optional<Character> lastOpt() {
        return Optional.of(last());
    }

    @Override
    public AbstractStringEx tail() {
        return init(new String(charArrTail(arr)));
    }

    @Override
    public AbstractStringEx prepend(char e) {
        return init(new String(prependToCharArray(toCharArray(), e)));
    }

    @SuppressWarnings("unchecked")
    protected <R> Collection<R> mapToOptimized(Function<Character, R> f) {
        AbstractStringEx cur = this;
        Collection<R> acc = JavaCollectionInterop.defaultCollection();
        for(;;)
            if (cur.isEmpty()) {
                return acc;
            } else {
                acc = newCollectionFromArray(prependToArray((R[]) acc.toArray(), f.apply(cur.head())));
                cur = cur.tail();
            }
    }

    protected AbstractStringEx mapOptimized(Function<Character, Character> f) {
        AbstractStringEx cur = this;
        AbstractStringEx acc = init("");
        for (;;)
            if (cur.isEmpty()) {
                return acc;
            } else {
                acc = acc.append(f.apply(cur.head()));
                cur = cur.tail();
            }
    }

    @Override
    public AbstractStringEx prepend(char[] chars) {
        return init(new String(mergeCharArr(this.toCharArray(), chars)));
    }

    @Override
    public AbstractStringEx prepend(Collection<Character> characters) {
        return append(toPrimitiveCharArr(characters));
    }

    @Override
    public AbstractStringEx prepend(String str) {
        return prepend(str.toCharArray());
    }

    @Override
    public AbstractStringEx prepend(BaseStringEx stringEx) {
        return init(new String(mergeCharArr(toCharArray(), stringEx.internal().toCharArray())));
    }

    AbstractStringEx flatMapOptimized(Function<Character, BaseStringEx> f) {
        AbstractStringEx cur = this;
        AbstractStringEx acc = init("");

        for (;;)
            if (cur.isEmpty()) {
                return acc;
            } else {
                acc = acc.prepend(f.apply(cur.head()));
                cur = cur.tail();
            }
    }

    @SuppressWarnings("unchecked")
    <R> Collection<R> flatMapToOptimized(Function<Character, Collection<R>> f) {
        AbstractStringEx cur = this;
        Collection<R> acc = JavaCollectionInterop.defaultCollection();

        for (;;) {
            if (cur.isEmpty()) {
                return acc;
            } else {
                acc = newCollectionFromArray(mergeGenericArr((R[]) f.apply(cur.head()).toArray(), (R[]) acc.toArray()));
                cur = cur.tail();
            }
        }
    }

    @Override
    public AbstractStringEx filter(Predicate<Character> p) {
        AbstractStringEx cur = this;
        AbstractStringEx acc = init("");

        for (;cur.nonEmpty(); cur = cur.tail()) {
            char curHead = cur.head();
            if (p.test(curHead))
                acc = acc.append(curHead);
        }

        return acc;
    }

    @Override
    public AbstractStringEx drop(int n) {
        AbstractStringEx cur = this;
        int cnt = n;

        for (;; cnt--) {
            if (cur.nonEmpty() && cnt > 0)
                cur = cur.tail();

            return cur;
        }
    }

    @Override
    public AbstractStringEx dropRight(int n) {
        return slice(0, length() - Math.max(n, 0));
    }

    @Override
    public AbstractStringEx filterNot(Predicate<Character> p) {
        return filter(p.negate());
    }

    @Override
    public AbstractStringEx append(char e) {
        return init(new String(appendToCharArray(arr, e)));
    }

    @Override
    public AbstractStringEx append(String str) {
        return init(internal() + str);
    }

    @Override
    public AbstractStringEx append(char[] chars) {
        return init(new String(mergeCharArr(arr, chars)));
    }

    @Override
    public AbstractStringEx append(BaseStringEx that) {
        return init(internal().concat(that.internal()));
    }

    @Override
    public AbstractStringEx append(Collection<Character> characters) {
        return append(toPrimitiveCharArr(characters));
    }

    @Override
    public AbstractStringEx capitalize() {
        return init(internal().toUpperCase());
    }

    @Override
    public boolean add(Character character) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Character> collection) {
        return false;
    }

    @Override
    public AbstractStringEx remove(char e) {
        return init(internal().replaceAll(String.valueOf(e), ""));
    }

    @Override
    public AbstractStringEx remove(String str) {
        return init(internal().replaceAll(str, ""));
    }

    @Override
    public AbstractStringEx remove(char[] chars) {
        return init(internal().replaceAll(new String(chars), ""));
    }

    @Override
    public AbstractStringEx remove(Collection<Character> characters) {
        return remove(toPrimitiveCharArr(characters));
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean removeIf(Predicate<? super Character> filter) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public Stream<Character> parallelStream() {
        return this.stream().parallel();
    }

    @Override
    public Stream<Character> stream() {
        return Stream.of((Character[]) toArray());
    }

    @Override
    public int compareTo(BaseStringEx characters) {
        return internal().compareTo(characters.internal());
    }

    @Override
    public IntStream chars() {
        return internal().chars();
    }

    @Override
    public IntStream codePoints() {
        return internal().codePoints();
    }

    @Override
    public void forEach(Consumer<? super Character> f) {
        AbstractStringEx cur = this;

        for (;;)
            if (cur.isEmpty()) {
                return;
            } else {
                f.accept(cur.head());
                cur = cur.tail();
            }
    }

    @Override
    public AbstractStringEx takeRight(int n) {
        return slice(length() - Math.max(n, 0), length());
    }

    @Override
    public AbstractStringEx padTo(int l, char e) {
        return padTo(l, "" + e);
    }

    @Override
    public AbstractStringEx padTo(int l, String str) {
        AbstractStringEx acc = init("");
        int len = length();
        int diff = l - len;
        acc = acc.append(this);

        for(;diff > 0; diff--)
            acc = acc.append(str);

        return acc;
    }

    @Override
    public <R> Collection<R> collect(Function<Character, R> f) {
        AbstractStringEx cur = this;
        Collection<R> coll = defaultCollection();

        for (;cur.nonEmpty();) {
            char curHead = cur.head();
            coll.add(f.apply(curHead));
            cur = cur.tail();
        }

        return coll;
    }

    @Override
    public <R> Optional<Collection<R>> collectOpt(Function<Character, R> f) {
        return Optional.of(collect(f));
    }

    private static <R> Predicate<R> functionToPredicate(Function<R, Boolean> function) {
        return function::apply;
    }

    public static <R> Function<R, Boolean> combine(Function<R, Boolean> f, Predicate<Boolean> pred) {
        return a -> pred.test(f.apply(a));
    }

    @SuppressWarnings("unchecked")
    private <R> Optional<R> collectFirst0(Function<Character, R> f) {
        R[] array = (R[]) collect(f).toArray();

        return array.length == 0 ? Optional.empty() : Optional.of(array[0]);
    }

    @Override
    public <R> Optional<R> collectFirst(Function<Character, R> f) {
        return collectFirst0(f);
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean forall(Predicate<Character> p) {
        AbstractStringEx cur = this;

        for (;cur.nonEmpty();) {
            if (p.test(cur.head()))
                return false;
            else cur = cur.tail();
        }
        return true;
    }

    @Override
    public int size() {
        return arr.length;
    }

    private String charSeqExToString(CharSequenceEx charSequenceEx) {
        CharSequenceEx cur = charSequenceEx;
        StringBuilder stringBuilder = new StringBuilder();

        for (;!cur.isEmpty(); cur = cur.tail())
            stringBuilder.append(cur.head());

        return stringBuilder.toString();
    }

    @Override
    public AbstractStringEx patch(int from, CharSequenceEx replace, int replaced) {
        return patch(from, charSeqExToString(replace), replaced);
    }

    @Override
    public AbstractStringEx patch(int from, String replace, int replaced) {
        AbstractStringEx acc = init("");
        int i = 0;

        Iterator<Character> iterator = iterator();

        for (;i < from && iterator.hasNext(); i++)
            acc = acc.append(iterator.next());

        acc = acc.append(replace);
        i = replaced;

        for (;i > 0 && iterator.hasNext(); i--)
            iterator.next();

        for(;iterator.hasNext();)
            acc = acc.append(iterator.next());

        return acc;
    }

    @Override
    public boolean contains(char c) {
        AbstractStringEx cur = this;

        for (;cur.nonEmpty();)
            if (c != cur.head())
                return false;
            else cur = cur.tail();

        return true;
    }

    @Override
    public boolean contains(String str) {
        return internal().contains(str);
    }

    @Override
    public boolean contains(CharSequenceEx charSequenceEx) {
        AbstractStringEx cur = this;

        for (;cur.nonEmpty();)
            if (charSequenceEx.head() != cur.head())
                return false;
            else cur = cur.tail();

        return true;
    }

    @Override
    public AbstractStringEx slice(int from, int until) {
        int selfLen = length();

        int start = Math.max(from, 0);

        if (until <= start || start >= selfLen)
            return init("");

        int end = Math.min(until, selfLen);
        return init(internal().substring(start, end));
    }

    @Override
    public AbstractStringEx takeWhile(Predicate<Character> p) {
        AbstractStringEx cur = this;
        AbstractStringEx acc = init("");

        char curHead = cur.head();
        for (;cur.nonEmpty() && p.test(curHead);) {
            acc = acc.append(curHead);
            cur = cur.tail();
            curHead = cur.head();
        }

        return acc;
    }

    @Override
    public AbstractStringEx takeUntil(Predicate<Character> p) {
        AbstractStringEx cur = this;
        AbstractStringEx acc = init("");
        char curHead = cur.head();

        for (;cur.nonEmpty() && p.test(curHead);) {
            curHead = cur.head();
            acc = acc.append(curHead);
            cur = cur.tail();
        }

        if (cur.nonEmpty() && p.test(curHead))
            acc = acc.append(curHead);

        return acc;
    }

    @Override
    public AbstractStringEx take(int n) {
        AbstractStringEx cur = this;
        AbstractStringEx acc = init("");
        int cnt = n;

        for (;; cnt--)
            if (cur.nonEmpty() && cnt > 0) {
                acc = acc.append(cur.head());
                cur = cur.tail();
            } else return acc;
    }

    @Override
    public Collection<Character> split(char sep) {
        return internal().chars().mapToObj(x -> (char) x).collect(Collectors.toList());
    }

    @Override
    public Pair<AbstractStringEx, AbstractStringEx> span(Predicate<Character> p) {
        AbstractStringEx self = this;
        int selfLen = length();
        int from = 0;
        int i = from;

        for (;i < selfLen && p.test(self.charAt(i));)
            i++;

        return splitAt(i - from);
    }

    @Override
    public abstract Pair<AbstractStringEx, AbstractStringEx> splitAt(int n);

    @Override
    public Collection<String> splitToStrings(String sep) {
        return Arrays.stream(internal().split(sep)).collect(Collectors.toList());
    }

    private Pair<Character, Character> minMaxPair() {
        return foldl(Pair.of(head(), head()), (pair, x) -> {
            char a = pair._1;
            char b = pair._2;

            return isLess(x, a) ? Pair.of(x, b)
                    : isGreater(x, b)
                        ? Pair.of(a, x)
                            : pair;
        });
    }

    @Override
    public char max() {
        return minMaxPair()._2;
    }

    private <R extends Comparable<R>> char minMaxByHelper(Function<Character, R> f,
                                                          BiPredicate<R, R> biPred) {
        if (isEmpty()) throw new UnsupportedOperationException("Empty");

        AbstractStringEx cur = this;

        R extr = null;
        char extrElem = 0;
        boolean isFirst = true;

        for (;cur.nonEmpty();) {
            char curHead = cur.head();
            R applied = f.apply(curHead);

            if (isFirst || biPred.test(applied, extr)) {
                extrElem = curHead;
                extr = applied;
                isFirst = false;
            }
            cur = cur.tail();
        }
        return extrElem;
    }

    @Override
    public <R extends Comparable<R>> char maxBy(Function<Character, R> f) {
        return minMaxByHelper(f, Ordering::isGreater);
    }

    @Override
    public char maxBy(Comparator<Character> comparator) {
        return minMaxByHelper(Function.identity(),
                (next, cand) -> comparator.compare(next, cand) > 0);
    }

    @Override
    public char min() {
        return minMaxPair()._1;
    }

    @Override
    public <R extends Comparable<R>> char minBy(Function<Character, R> f) {
        return minMaxByHelper(f, Ordering::isLess);
    }

    @Override
    public char minBy(Comparator<Character> comparator) {
        return minMaxByHelper(Function.identity(),
                (next, cand) -> comparator.compare(next, cand) < 0);
    }

    @Override
    public int count(Predicate<Character> p) {
        AbstractStringEx cur = this;
        int cnt = 0;

        for (;;)
            if (cur.nonEmpty() && p.test(cur.head())) {
                cur = cur.tail();
                cnt++;
            }
            else return cnt;
    }

    @Override
    public Optional<BaseStringEx> tailOpt() {
        return Optional.of(tail());
    }

    @Override
    public Pair<String, String> spanToString(Predicate<Character> p) {
        Pair<AbstractStringEx, AbstractStringEx> spanned = span(p);
        return Pair.of(spanned._1.internal(), spanned._2.internal());
    }

    @Override
    public AbstractStringEx concat(int n) {
        AbstractStringEx cur = this;
        StringBuilder stringBuilder = new StringBuilder(), acc = new StringBuilder();
        int cnt = n;

        for(;cur.nonEmpty();) {
            stringBuilder.append(cur.head());
            cur = cur.tail();
        }

        String temp = stringBuilder.toString();
        for (;cnt != 0; --cnt)
            acc.append(temp);

        return init(acc.toString());
    }

    @Override
    public Pair<String, String> splitStringAt(int n) {
        Pair<AbstractStringEx, AbstractStringEx> pair = splitAt(n);
        return Pair.of(charSeqExToString(pair._1), charSeqExToString(pair._2));
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Character) return contains((char) o);
        else if (o instanceof String) return contains((String) o);
        else if (o instanceof StringEx) return contains((StringEx) o);
        else return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public char charAt(int i) {
        return arr[i];
    }

    @Override
    public Object[] toArray() {
        int length = Array.getLength(arr);
        Object[] objArr = new Object[length];
        for(int i = 0; i < length; i++)
            objArr[i] = Array.get(arr, i);

        return objArr;
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return null;
    }

    @Override
    public Spliterator<Character> spliterator() {
        return null;
    }

    @Override
    public int length() {
        return size();
    }

    @Override
    public AbstractStringEx subSequence(int i, int i1) {
        return init((String) internal().subSequence(i, i1));
    }

    @Override
    public <R> R fold(R z, BiFunction<R, Character, R> f) {
        return foldl(z, f);
    }

    @Override
    public <R> R foldr(R z, BiFunction<Character, R, R> f) {
        AbstractStringEx cur = this.reverse();
        R acc = z;

        for (;;)
            if (cur.isEmpty()) {
                return acc;
            } else {
                acc = f.apply(cur.head(), acc);
                cur = cur.tail();
            }
    }

    @Override
    public <R> R foldl(R z, BiFunction<R, Character, R> f) {
        AbstractStringEx cur = this;
        R acc = z;

        for (;;)
            if (cur.isEmpty()) {
                return acc;
            } else {
                acc = f.apply(acc, cur.head());
                cur = cur.tail();
            }
    }

    @Override
    public <R> R foldlStringEx(R z, BiFunction<R, BaseStringEx, R> f) {
        AbstractStringEx cur = this;
        R acc = z;

        for (;;)
            if (cur.isEmpty()) {
                return acc;
            } else {
                acc = f.apply(acc, cur);
                cur = cur.tail();
            }
    }

    @Override
    public <R> R foldrStringEx(R z, BiFunction<BaseStringEx, R, R> f) {
        AbstractStringEx cur = this;
        R acc = z;

        for (;;)
            if (cur.isEmpty()) {
                return acc;
            } else {
                acc = f.apply(cur, acc);
                cur = cur.tail();
            }
    }

    @Override
    public <R> R foldStringEx(R z, BiFunction<R, BaseStringEx, R> f) {
        return foldlStringEx(z, f);
    }

    @Override
    public String toString() {
        return "StringEx(" + internal() + ")";
    }
}