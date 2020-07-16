package io.cat.ai.stringex.core;

import io.cat.ai.stringex.util.Pair;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Predicate;

public interface CharSequenceEx extends CharSequence, Iterable<Character>, Serializable {

    char head();

    CharSequenceEx tail();

    char last();

    boolean contains(char c);

    boolean contains(CharSequenceEx str);

    CharSequenceEx take(int n);

    CharSequenceEx takeWhile(Predicate<Character> p);

    CharSequenceEx takeUntil(Predicate<Character> p);

    CharSequenceEx takeRight(int n);

    CharSequenceEx drop(int n);

    CharSequenceEx dropRight(int n);

    CharSequenceEx slice(int from, int until);

    CharSequenceEx reverse();

    Collection<Character> split(char sep);

    Pair<? extends CharSequenceEx, ? extends CharSequenceEx> splitAt(int n);

    CharSequenceEx remove(char elem);

    Pair<? extends CharSequenceEx, ? extends CharSequenceEx> span(Predicate<Character> p);

    CharSequenceEx padTo(int l, char e);

    CharSequenceEx padTo(int l, String str);

    CharSequenceEx patch(int from, String replace, int replaced);

    CharSequenceEx patch(int from, CharSequenceEx replace, int replaced);

    boolean forall(Predicate<Character> f);

    boolean isEmpty();
}
