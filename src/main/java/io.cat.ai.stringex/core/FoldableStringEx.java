package io.cat.ai.stringex.core;

import java.util.function.BiFunction;

public interface FoldableStringEx extends Foldable<Character> {

    <R> R foldStringEx(R z, BiFunction<R, BaseStringEx, R> f);

    <R> R foldlStringEx(R z, BiFunction<R, BaseStringEx, R> f);

    <R> R foldrStringEx(R z, BiFunction<BaseStringEx, R, R> f);
}