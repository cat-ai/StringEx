package io.cat.ai.stringex.util;

import java.util.Objects;

public class Pair<A, B> {

  public final A _1;
  public final B _2;

  private Pair(A a, B b) {
    _1 = a;
    _2 = b;
  }

  public static <A, B> Pair<A, B> of(A a, B b) {
    return new Pair<>(a, b);
  }

  @Override
  public String toString() {
    return "Pair(" + _1 + ", " + _2 + ')';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Pair)) return false;
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return _1.equals(pair._1) &&
            _2.equals(pair._2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_1, _2);
  }
}