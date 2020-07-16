package io.cat.ai.stringex.core;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public interface StringOps {

     int codePointAt(int index);

     int codePointBefore(int index);

     int codePointCount(int beginIndex, int endIndex);

     int offsetByCodePoints(int index, int codePointOffset);

     byte[] getBytes(String charsetName) throws UnsupportedEncodingException;

     byte[] getBytes(Charset charset);

     byte[] getBytes();

     boolean contentEquals(StringBuffer sb);

     boolean contentEquals(CharSequence cs);

     boolean equalsIgnoreCase(String anotherString);

     int compareToIgnoreCase(String str);

     boolean startsWith(String prefix, int toffset);

     boolean startsWith(String prefix);

     boolean endsWith(String suffix);

     int indexOf(int ch);

     int indexOf(int ch, int fromIndex);

     int lastIndexOf(int ch);

     int lastIndexOf(int ch, int fromIndex);

     int indexOf(String str);

     int indexOf(String str, int fromIndex);

     int lastIndexOf(String str, int fromIndex);

     StringOps substring(int beginIndex);

     StringOps substring(int beginIndex, int endIndex);

     StringOps replace(char oldChar, char newChar);

     boolean matches(String regex);

     StringOps replaceFirst(String regex, String replacement);

     StringOps replaceAll(String regex, String replacement);

     StringOps replace(CharSequence target, CharSequence replacement);

     StringOps trim();
}