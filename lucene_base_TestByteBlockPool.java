package org.apache.lucene.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestByteBlockPool {

  private ByteBlockPool byteBlockPool;
  private ByteBlockPool.DirectAllocator directAllocator;

  @Before
  public void setUp() {
    directAllocator = new ByteBlockPool.DirectAllocator();
    byteBlockPool = new ByteBlockPool(directAllocator);
  }

  @Test
  public void testNextBuffer() {
    byteBlockPool.nextBuffer();
    assertNotNull(byteBlockPool.buffer);
    assertEquals(0, byteBlockPool.byteUpto);
    assertEquals(0, byteBlockPool.byteOffset);
  }

  @Test
  public void testResetWithoutReuse() {
    byteBlockPool.nextBuffer();
    byteBlockPool.reset(true, false);
    assertNull(byteBlockPool.buffer);
    assertEquals(-1, byteBlockPool.bufferUpto);
    assertEquals(ByteBlockPool.BYTE_BLOCK_SIZE, byteBlockPool.byteUpto);
    assertEquals(-ByteBlockPool.BYTE_BLOCK_SIZE, byteBlockPool.byteOffset);
  }

  @Test
  public void testResetWithReuse() {
    byteBlockPool.nextBuffer();
    byteBlockPool.reset(true, true);
    assertNotNull(byteBlockPool.buffer);
    assertEquals(0, byteBlockPool.bufferUpto);
    assertEquals(0, byteBlockPool.byteUpto);
    assertEquals(0, byteBlockPool.byteOffset);
  }

  @Test
  public void testAppendBytes() {
    byteBlockPool.nextBuffer();
    byte[] bytes = new byte[] {1, 2, 3, 4, 5};
    byteBlockPool.append(bytes);
    assertEquals(5, byteBlockPool.byteUpto);
    for (int i = 0; i < bytes.length; i++) {
      assertEquals(bytes[i], byteBlockPool.buffer[i]);
    }
  }

  @Test
  public void testAppendBytesRef() {
    byteBlockPool.nextBuffer();
    byte[] bytes = new byte[] {10, 20, 30, 40, 50};
    BytesRef bytesRef = new BytesRef(bytes);
    byteBlockPool.append(bytesRef);
    assertEquals(5, byteBlockPool.byteUpto);
    for (int i = 0; i < bytesRef.length; i++) {
      assertEquals(bytesRef.bytes[i], byteBlockPool.buffer[i]);
    }
  }

  @Test
  public void testReadBytes() {
    byteBlockPool.nextBuffer();
    byte[] bytes = new byte[] {5, 6, 7, 8, 9};
    byteBlockPool.append(bytes);
    byte[] readBytes = new byte[5];
    byteBlockPool.readBytes(0, readBytes, 0, 5);
    assertArrayEquals(bytes, readBytes);
  }

  @Test
  public void testReadByte() {
    byteBlockPool.nextBuffer();
    byte[] bytes = new byte[] {100, 101, 102};
    byteBlockPool.append(bytes);
    byte value = byteBlockPool.readByte(1);
    assertEquals(101, value);
  }

  @Test
  public void testRamBytesUsed() {
    byteBlockPool.nextBuffer();
    long ramBytesUsed = byteBlockPool.ramBytesUsed();
    assertTrue(ramBytesUsed > 0);
  }

  @Test
  public void testGetPosition() {
    byteBlockPool.nextBuffer();
    byte[] bytes = new byte[] {1, 2, 3};
    byteBlockPool.append(bytes);
    long position = byteBlockPool.getPosition();
    assertEquals(3, position);
  }

  @Test
  public void testGetBuffer() {
    byteBlockPool.nextBuffer();
    byte[] buffer = byteBlockPool.getBuffer(0);
    assertNotNull(buffer);
    assertEquals(ByteBlockPool.BYTE_BLOCK_SIZE, buffer.length);
  }
}