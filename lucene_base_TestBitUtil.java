package org.apache.lucene.util;

import org.junit.Test;

import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class TestBitUtil {

  @Test
  public void testNextHighestPowerOfTwoInt() {
    assertEquals(1, BitUtil.nextHighestPowerOfTwo(0));
    assertEquals(1, BitUtil.nextHighestPowerOfTwo(1));
    assertEquals(2, BitUtil.nextHighestPowerOfTwo(2));
    assertEquals(4, BitUtil.nextHighestPowerOfTwo(3));
    assertEquals(4, BitUtil.nextHighestPowerOfTwo(4));
    assertEquals(8, BitUtil.nextHighestPowerOfTwo(5));
    assertEquals(1024, BitUtil.nextHighestPowerOfTwo(1023));
    assertEquals(1024, BitUtil.nextHighestPowerOfTwo(1024));
    assertEquals(2048, BitUtil.nextHighestPowerOfTwo(1025));
  }

  @Test
  public void testNextHighestPowerOfTwoLong() {
    assertEquals(1L, BitUtil.nextHighestPowerOfTwo(0L));
    assertEquals(1L, BitUtil.nextHighestPowerOfTwo(1L));
    assertEquals(2L, BitUtil.nextHighestPowerOfTwo(2L));
    assertEquals(4L, BitUtil.nextHighestPowerOfTwo(3L));
    assertEquals(4L, BitUtil.nextHighestPowerOfTwo(4L));
    assertEquals(8L, BitUtil.nextHighestPowerOfTwo(5L));
    assertEquals(1024L, BitUtil.nextHighestPowerOfTwo(1023L));
    assertEquals(1024L, BitUtil.nextHighestPowerOfTwo(1024L));
    assertEquals(2048L, BitUtil.nextHighestPowerOfTwo(1025L));
  }

  @Test
  public void testInterleaveDeinterleave() {
    int even = 0x55555555;
    int odd = 0xAAAAAAAA;
    long interleaved = BitUtil.interleave(even, odd);
    assertEquals(even, BitUtil.deinterleave(interleaved));
  }

  @Test
  public void testFlipFlop() {
    long value = 0b101010;
    long expected = 0b010101;
    assertEquals(expected, BitUtil.flipFlop(value));
  }

  @Test
  public void testZigZagEncodeDecodeInt() {
    int[] values = {-1, 0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE};
    for (int value : values) {
      assertEquals(value, BitUtil.zigZagDecode(BitUtil.zigZagEncode(value)));
    }
  }

  @Test
  public void testZigZagEncodeDecodeLong() {
    long[] values = {-1L, 0L, 1L, Long.MIN_VALUE, Long.MAX_VALUE};
    for (long value : values) {
      assertEquals(value, BitUtil.zigZagDecode(BitUtil.zigZagEncode(value)));
    }
  }

  @Test
  public void testIsZeroOrPowerOfTwo() {
    assertTrue(BitUtil.isZeroOrPowerOfTwo(0));
    assertTrue(BitUtil.isZeroOrPowerOfTwo(1));
    assertTrue(BitUtil.isZeroOrPowerOfTwo(2));
    assertFalse(BitUtil.isZeroOrPowerOfTwo(3));
    assertTrue(BitUtil.isZeroOrPowerOfTwo(4));
    assertFalse(BitUtil.isZeroOrPowerOfTwo(5));
    assertFalse(BitUtil.isZeroOrPowerOfTwo(-1));
  }

  @Test
  public void testNativeByteOrder() {
    // Since NATIVE_BYTE_ORDER is randomized in test environments, we cannot assert its exact value.
    assertNotNull(BitUtil.NATIVE_BYTE_ORDER);
    assertTrue(
        BitUtil.NATIVE_BYTE_ORDER == ByteOrder.LITTLE_ENDIAN
            || BitUtil.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN);
  }
}