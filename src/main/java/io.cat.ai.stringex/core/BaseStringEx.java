package io.cat.ai.stringex.core;

import io.cat.ai.stringex.util.Pair;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface BaseStringEx extends CharSequenceEx,
        FoldableStringEx,
        StringExOps,
        StringExConverterOps,
        Comparable<BaseStringEx>,
        Collection<Character> {

    String internal();

    BaseStringEx map(Function<Character, Character> f);

    <R> Collection<R> mapTo(Function<Character, R> f);

    BaseStringEx flatMap(Function<Character, BaseStringEx> f);

    <R> Collection<R> flatMapTo(Function<Character, Collection<R>> f);

    BaseStringEx filter(Predicate<Character> p);

    BaseStringEx filterNot(Predicate<Character> p);

    BaseStringEx sorted();

    BaseStringEx sorted(Comparator<? super Character> c);

    <A extends Comparable<A>> BaseStringEx sortBy(Function<Character, A> f);

    BaseStringEx sortBy(Comparator<? super Character> c);

    BaseStringEx sortWith(Predicate<Pair<Character, Character>> pred);

    <R> Collection<R> collect(Function<Character, R> f);

    <R> Optional<Collection<R>> collectOpt(Function<Character, R> f);

    <R> Optional<R> collectFirst(Function<Character, R> f);

    Collection<String> splitToStrings(String sep);

    Pair<String, String> splitStringAt(int n);

    Pair<String, String> spanToString(Predicate<Character> p);

    char min();

    <R extends Comparable<R>> char minBy(Function<Character, R> f);

    char minBy(Comparator<Character> f);

    char max();

    <R extends Comparable<R>> char maxBy(Function<Character, R> f);

    char maxBy(Comparator<Character> f);

    default boolean nonEmpty() {  return !isEmpty(); }

    boolean contains(String str);
}