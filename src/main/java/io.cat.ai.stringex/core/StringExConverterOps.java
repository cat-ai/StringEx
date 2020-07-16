package io.cat.ai.stringex.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public interface StringExConverterOps {

    boolean asBoolean() throws Exception;

    byte asByte() ;

    short asShort() throws Exception;

    int asInt() throws Exception;

    float asFloat() throws Exception;

    double asDouble() throws Exception;

    long asLong() throws Exception;

    BigDecimal asBigDecimal();

    BigDecimal asBigDecimal(MathContext mc) throws Exception;

    BigInteger asBigInteger() throws Exception;

    BigInteger asBigInteger(int radix) throws Exception;
}