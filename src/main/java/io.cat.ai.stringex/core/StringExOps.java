package io.cat.ai.stringex.core;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public interface StringExOps {

    Optional<Character> headOpt();

    Optional<BaseStringEx> tailOpt();

    Optional<Character> lastOpt();

    BaseStringEx concat(int n);

    BaseStringEx append(char e);

    BaseStringEx append(String str);

    BaseStringEx append(BaseStringEx stringEx);

    BaseStringEx append(char[] chars);

    BaseStringEx append(Collection<Character> characters);

    BaseStringEx prepend(char e);

    BaseStringEx prepend(String str);

    BaseStringEx prepend(BaseStringEx stringEx);

    BaseStringEx prepend(char[] chars);

    BaseStringEx prepend(Collection<Character> characters);

    BaseStringEx remove(char e);

    BaseStringEx remove(String str);

    BaseStringEx remove(char[] chars);

    BaseStringEx remove(Collection<Character> characters);

    BaseStringEx capitalize();

    int count(Predicate<Character> f);
}