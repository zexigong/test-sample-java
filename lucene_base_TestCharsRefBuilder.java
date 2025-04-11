package org.apache.lucene.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestCharsRefBuilder {

    private CharsRefBuilder builder;

    @Before
    public void setUp() {
        builder = new CharsRefBuilder();
    }

    @Test
    public void testInitialState() {
        assertEquals(0, builder.length());
        assertNotNull(builder.chars());
    }

    @Test
    public void testAppendCharSequence() {
        builder.append("Hello");
        assertEquals(5, builder.length());
        assertEquals('H', builder.charAt(0));
        assertEquals('o', builder.charAt(4));
    }

    @Test
    public void testAppendNullCharSequence() {
        builder.append((CharSequence) null);
        assertEquals(4, builder.length());
        assertEquals('n', builder.charAt(0));
        assertEquals('l', builder.charAt(3));
    }

    @Test
    public void testAppendChar() {
        builder.append('H');
        builder.append('i');
        assertEquals(2, builder.length());
        assertEquals('H', builder.charAt(0));
        assertEquals('i', builder.charAt(1));
    }

    @Test
    public void testSetCharAt() {
        builder.append("Hello");
        builder.setCharAt(1, 'a');
        assertEquals('a', builder.charAt(1));
    }

    @Test
    public void testClear() {
        builder.append("Hello");
        builder.clear();
        assertEquals(0, builder.length());
    }

    @Test
    public void testCopyChars() {
        CharsRef ref = new CharsRef("Hello");
        builder.copyChars(ref);
        assertEquals(5, builder.length());
        assertEquals('H', builder.charAt(0));
        assertEquals('o', builder.charAt(4));
    }

    @Test
    public void testCopyUTF8Bytes() {
        byte[] utf8Bytes = "Hello".getBytes();
        builder.copyUTF8Bytes(utf8Bytes, 0, utf8Bytes.length);
        assertEquals(5, builder.length());
        assertEquals('H', builder.charAt(0));
        assertEquals('o', builder.charAt(4));
    }

    @Test
    public void testToCharsRef() {
        builder.append("Hello");
        CharsRef ref = builder.toCharsRef();
        assertEquals(5, ref.length);
        assertEquals('H', ref.chars[0]);
        assertEquals('o', ref.chars[4]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEqualsThrowsException() {
        builder.equals(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHashCodeThrowsException() {
        builder.hashCode();
    }

    @Test
    public void testToString() {
        builder.append("Hello");
        assertEquals("Hello", builder.toString());
    }
}