package io.cat.ai.stringex;

import io.cat.ai.stringex.core.BaseStringEx;
import io.cat.ai.stringex.util.Ordering;
import io.cat.ai.stringex.util.Pair;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;

import static io.cat.ai.stringex.StringExDsl.*;
import static org.junit.Assert.assertEquals;

public class CommonTest {

    private Random random = new Random();

    private static <A> Set<A> JSet(A c) {
        Set<A> set = new HashSet<>();
        set.add(c);
        return set;
    }

    private AbstractStringEx qsort(AbstractStringEx stringEx) {
        switch (stringEx.length()) {
            case 0:
            case 1:
                return stringEx;
            case 2:
            default:
                char pivot = stringEx.charAt(random.nextInt(stringEx.length()));
                return qsort(stringEx.filter(x -> Ordering.isLess(x, pivot)))
                        .append(stringEx.filter(x -> Ordering.isEqual(x, pivot)))
                        .append(qsort(stringEx.filter(x -> Ordering.isGreater(x, pivot)))
                        );
        }
    }

    @Test
    public void shouldReturnTrueForMinMax() {
        AbstractStringEx stringEx = StringEx("12345678");
        assertEquals(stringEx.max(), '8');
        assertEquals(stringEx.min(), '1');
    }

    @Test
    public void shouldReturnTrueComparingSortedWithUnsorted() {
        assertEquals(StringEx("STRING").sorted(), StringEx("GINRST"));
    }

    @Test
    public void shouldReturnTrueCollectingChars() {
        Collection<Integer> col = Arrays.asList(1, 1, 2, 3, 0, 4, 4, 4, 4, 0);

        assertEquals(
                StringEx("AABCDEEEEF").collect(__ -> {
                    switch (__) {
                        case 'A':
                            return 1;
                        case 'B':
                            return 2;
                        case 'C':
                            return 3;
                        case 'E':
                            return 4;
                        default:
                            return 0;
                    }
                }),

                col);
    }

    @Test
    public void shouldReturnOptional777UsingCollectFirst() {
        assertEquals(
                StringEx("TTTTTThreshold").collectFirst(__ -> {
                    switch (__) {
                        case 'a': return 1;
                        case 'b': return 2;
                        case 'd': return 3;
                        case 'T': return 777;
                        default:  return 0;
                    }
                }),

                Optional.of(777)
        );
    }

    @Test
    public void shouldBeEqualAfterSortingWithQsort() {
        assertEquals(qsort(StringEx("87654321")), StringEx("12345678"));
    }

    @Test
    public void shouldBeEqualWithPartitionFunc() {
        assertEquals(
                StringEx("String").partition(__ -> __ == 'r'),
                Pair.of(StringEx('r'), StringEx("Sting"))
        );
    }

    @Test
    public void shouldBeEqualTailOpt() {
        assertEquals(StringEx("String").tailOpt(), Optional.of(StringEx("tring")));
    }

    @Test
    public void shouldCutZerosAtIndexWithPatchFunc() {
        assertEquals(
                StringEx("2020\\07\\08").patch(5, StringEx(""), 1)
                        .patch(7, StringEx(""), 1),
                StringEx("2020\\7\\8"));
    }

    @Test
    public void shouldReturn22WithFoldrFunc() {
        assertEquals(22, (int) StringEx("20").foldr(20, (x, y) -> x - y));
    }

    @Test
    public void shoudBeEqualWithPadToFunc() {
        assertEquals(StringEx("1234").reverse().padTo(7, '0'), StringEx("4321000"));
    }

    @Test
    public void functorIdentityLaw() {
        assertEquals(StringEx("STRING").map(Function.identity()) , StringEx("STRING"));
        assertEquals(StringEx("123").map(__ -> __) , StringEx("123"));
    }

    @Test
    public void functorAssociativeLaw() {
        Function<Character, Character> f = __ -> (char) (__ + 10);
        Function<Character, Character> g = __ -> (char) (__ + 20);

        assertEquals(StringEx("Hello").map(f).map(g), StringEx("Hello").map(f.andThen(g)));
    }

    @Test
    public void monadLeftUnitLawAssociativeLaw() {
        Function<Character, Character> f = __ -> (char) (__ ^ 0x20);
        assertEquals(StringEx("STRING").flatMap(x -> StringEx(f.apply(x))), StringEx("string"));
    }

    @Test
    public void monadRightUnitLawAssociativeLaw() {
        Function<Character, Character> f = __ -> (char) (__ & 0x5f);
        assertEquals(StringEx("test_string").flatMap(x -> StringEx(f.apply(x))), StringEx("TEST_STRING"));
    }

    @Test
    public void monadAssociativityLaw() {
        StringEx stringExM = StringEx("five");
        Function<Character, BaseStringEx> toUpperCaseF  = __ -> StringEx((char) (__ & 0x5f));
        Function<Character, BaseStringEx> charHashCodeF = __ -> StringEx(String.valueOf(__.hashCode()));

        BaseStringEx left = stringExM.flatMap(toUpperCaseF).flatMap(charHashCodeF);
        BaseStringEx right = stringExM.flatMap(x -> toUpperCaseF.apply(x).flatMap(charHashCodeF));

        assertEquals(left, right);
    }

}