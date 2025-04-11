package org.apache.lucene.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCharsRef {

    @Test
    public void testDefaultConstructor() {
        CharsRef charsRef = new CharsRef();
        assertNotNull(charsRef.chars);
        assertEquals(0, charsRef.length);
        assertEquals(0, charsRef.offset);
    }

    @Test
    public void testCapacityConstructor() {
        CharsRef charsRef = new CharsRef(10);
        assertNotNull(charsRef.chars);
        assertEquals(10, charsRef.chars.length);
    }

    @Test
    public void testArrayConstructor() {
        char[] chars = {'a', 'b', 'c'};
        CharsRef charsRef = new CharsRef(chars, 1, 2);
        assertArrayEquals(chars, charsRef.chars);
        assertEquals(1, charsRef.offset);
        assertEquals(2, charsRef.length);
    }

    @Test
    public void testStringConstructor() {
        CharsRef charsRef = new CharsRef("abc");
        assertEquals(3, charsRef.length);
        assertEquals(0, charsRef.offset);
        assertEquals("abc", charsRef.toString());
    }

    @Test
    public void testClone() {
        CharsRef original = new CharsRef("abc");
        CharsRef clone = original.clone();
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }

    @Test
    public void testHashCode() {
        CharsRef charsRef1 = new CharsRef("abc");
        CharsRef charsRef2 = new CharsRef("abc");
        assertEquals(charsRef1.hashCode(), charsRef2.hashCode());
    }

    @Test
    public void testEquals() {
        CharsRef charsRef1 = new CharsRef("abc");
        CharsRef charsRef2 = new CharsRef("abc");
        CharsRef charsRef3 = new CharsRef("abcd");
        assertTrue(charsRef1.equals(charsRef2));
        assertFalse(charsRef1.equals(charsRef3));
    }

    @Test
    public void testCompareTo() {
        CharsRef charsRef1 = new CharsRef("abc");
        CharsRef charsRef2 = new CharsRef("abc");
        CharsRef charsRef3 = new CharsRef("abd");
        assertEquals(0, charsRef1.compareTo(charsRef2));
        assertTrue(charsRef1.compareTo(charsRef3) < 0);
    }

    @Test
    public void testToString() {
        CharsRef charsRef = new CharsRef("abc");
        assertEquals("abc", charsRef.toString());
    }

    @Test
    public void testLength() {
        CharsRef charsRef = new CharsRef("abc");
        assertEquals(3, charsRef.length());
    }

    @Test
    public void testCharAt() {
        CharsRef charsRef = new CharsRef("abc");
        assertEquals('a', charsRef.charAt(0));
        assertEquals('b', charsRef.charAt(1));
        assertEquals('c', charsRef.charAt(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCharAtOutOfBounds() {
        CharsRef charsRef = new CharsRef("abc");
        charsRef.charAt(3);
    }

    @Test
    public void testSubSequence() {
        CharsRef charsRef = new CharsRef("abc");
        CharSequence subSeq = charsRef.subSequence(1, 3);
        assertEquals("bc", subSeq.toString());
    }

    @Test
    public void testDeepCopyOf() {
        CharsRef original = new CharsRef("abc");
        CharsRef copy = CharsRef.deepCopyOf(original);
        assertNotSame(original, copy);
        assertEquals(original, copy);
    }

    @Test
    public void testIsValid() {
        CharsRef validCharsRef = new CharsRef("abc");
        assertTrue(validCharsRef.isValid());
        
        try {
            CharsRef invalidCharsRef = new CharsRef(null, 0, 0);
            invalidCharsRef.isValid();
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }
}