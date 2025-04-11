package org.apache.lucene.util;

import org.junit.Test;
import org.junit.Before;
import java.util.Comparator;

import static org.junit.Assert.*;

public class TestArrayUtil {

    private char[] charArray;
    private int[] intArray;
    private long[] longArray;
    private byte[] byteArray;

    @Before
    public void setUp() {
        charArray = new char[]{'1', '2', '3', '4', '5'};
        intArray = new int[]{1, 2, 3, 4, 5};
        longArray = new long[]{1L, 2L, 3L, 4L, 5L};
        byteArray = new byte[]{1, 2, 3, 4, 5};
    }

    @Test
    public void testParseIntCharArray() {
        assertEquals(123, ArrayUtil.parseInt(new char[]{'1', '2', '3'}, 0, 3));
        assertEquals(-123, ArrayUtil.parseInt(new char[]{'-', '1', '2', '3'}, 0, 4));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntCharArrayException() {
        ArrayUtil.parseInt(new char[]{'a', 'b', 'c'}, 0, 3);
    }

    @Test
    public void testOversize() {
        assertTrue(ArrayUtil.oversize(5, Integer.BYTES) > 5);
        assertTrue(ArrayUtil.oversize(10, Byte.BYTES) > 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOversizeNegative() {
        ArrayUtil.oversize(-1, Integer.BYTES);
    }

    @Test
    public void testGrowExact() {
        int[] grownArray = ArrayUtil.growExact(intArray, 10);
        assertEquals(10, grownArray.length);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 0, 0, 0, 0, 0}, grownArray);
    }

    @Test
    public void testGrow() {
        int[] grownArray = ArrayUtil.grow(intArray, 10);
        assertTrue(grownArray.length >= 10);
    }

    @Test
    public void testGrowNoCopy() {
        int[] grownArray = ArrayUtil.growNoCopy(intArray, 10);
        assertTrue(grownArray.length >= 10);
        assertEquals(0, grownArray[5]);
    }

    @Test
    public void testCopyArray() {
        int[] copiedArray = ArrayUtil.copyArray(intArray);
        assertArrayEquals(intArray, copiedArray);
    }

    @Test
    public void testCopyOfSubArray() {
        int[] subArray = ArrayUtil.copyOfSubArray(intArray, 1, 3);
        assertArrayEquals(new int[]{2, 3}, subArray);
    }

    @Test
    public void testHashCode() {
        assertEquals(ArrayUtil.hashCode(charArray, 0, 5), ArrayUtil.hashCode(charArray, 0, 5));
    }

    @Test
    public void testSwap() {
        Integer[] array = {1, 2, 3};
        ArrayUtil.swap(array, 0, 2);
        assertArrayEquals(new Integer[]{3, 2, 1}, array);
    }

    @Test
    public void testIntroSort() {
        Integer[] array = {3, 2, 1};
        ArrayUtil.introSort(array);
        assertArrayEquals(new Integer[]{1, 2, 3}, array);
    }

    @Test
    public void testTimSort() {
        Integer[] array = {3, 2, 1};
        ArrayUtil.timSort(array);
        assertArrayEquals(new Integer[]{1, 2, 3}, array);
    }

    @Test
    public void testSelect() {
        Integer[] array = {3, 1, 2};
        ArrayUtil.select(array, 0, 3, 1, Comparator.naturalOrder());
        assertEquals(2, (int) array[1]);
    }

    @Test
    public void testByteArrayComparator() {
        byte[] a = {1, 2, 3, 4};
        byte[] b = {1, 2, 3, 5};
        ArrayUtil.ByteArrayComparator comparator = ArrayUtil.getUnsignedComparator(4);
        assertTrue(comparator.compare(a, 0, b, 0) < 0);
    }
}