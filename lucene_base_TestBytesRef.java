package org.apache.lucene.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestBytesRef {

  @Test
  public void testEmptyBytesRef() {
    BytesRef bytesRef = new BytesRef();
    assertNotNull(bytesRef.bytes);
    assertEquals(0, bytesRef.offset);
    assertEquals(0, bytesRef.length);
    assertTrue(bytesRef.isValid());
  }

  @Test
  public void testBytesRefWithArray() {
    byte[] data = {1, 2, 3, 4};
    BytesRef bytesRef = new BytesRef(data);
    assertArrayEquals(data, bytesRef.bytes);
    assertEquals(0, bytesRef.offset);
    assertEquals(data.length, bytesRef.length);
    assertTrue(bytesRef.isValid());
  }

  @Test
  public void testBytesRefWithArrayAndOffset() {
    byte[] data = {1, 2, 3, 4};
    BytesRef bytesRef = new BytesRef(data, 1, 2);
    assertEquals(1, bytesRef.offset);
    assertEquals(2, bytesRef.length);
    assertTrue(bytesRef.isValid());
  }

  @Test
  public void testBytesRefFromCharSequence() {
    String text = "hello";
    BytesRef bytesRef = new BytesRef(text);
    assertEquals(text, bytesRef.utf8ToString());
    assertTrue(bytesRef.isValid());
  }

  @Test
  public void testBytesEquals() {
    byte[] data1 = {1, 2, 3, 4};
    byte[] data2 = {1, 2, 3, 4};
    BytesRef bytesRef1 = new BytesRef(data1);
    BytesRef bytesRef2 = new BytesRef(data2);
    assertTrue(bytesRef1.bytesEquals(bytesRef2));
  }

  @Test
  public void testClone() {
    byte[] data = {1, 2, 3, 4};
    BytesRef bytesRef = new BytesRef(data);
    BytesRef cloned = bytesRef.clone();
    assertEquals(bytesRef, cloned);
    assertArrayEquals(bytesRef.bytes, cloned.bytes);
    assertEquals(bytesRef.offset, cloned.offset);
    assertEquals(bytesRef.length, cloned.length);
  }

  @Test
  public void testHashCode() {
    byte[] data = {1, 2, 3, 4};
    BytesRef bytesRef1 = new BytesRef(data);
    BytesRef bytesRef2 = new BytesRef(data);
    assertEquals(bytesRef1.hashCode(), bytesRef2.hashCode());
  }

  @Test
  public void testEquals() {
    byte[] data1 = {1, 2, 3, 4};
    byte[] data2 = {1, 2, 3, 4};
    byte[] data3 = {4, 3, 2, 1};
    BytesRef bytesRef1 = new BytesRef(data1);
    BytesRef bytesRef2 = new BytesRef(data2);
    BytesRef bytesRef3 = new BytesRef(data3);
    assertTrue(bytesRef1.equals(bytesRef2));
    assertFalse(bytesRef1.equals(bytesRef3));
    assertFalse(bytesRef1.equals(null));
    assertFalse(bytesRef1.equals(new Object()));
  }

  @Test
  public void testUtf8ToString() {
    String text = "hello";
    BytesRef bytesRef = new BytesRef(text);
    assertEquals(text, bytesRef.utf8ToString());
  }

  @Test
  public void testToString() {
    byte[] data = {1, 2, 3};
    BytesRef bytesRef = new BytesRef(data);
    assertEquals("[1 2 3]", bytesRef.toString());
  }

  @Test
  public void testCompareTo() {
    byte[] data1 = {1, 2, 3};
    byte[] data2 = {1, 2, 4};
    BytesRef bytesRef1 = new BytesRef(data1);
    BytesRef bytesRef2 = new BytesRef(data2);
    assertTrue(bytesRef1.compareTo(bytesRef2) < 0);
    assertTrue(bytesRef2.compareTo(bytesRef1) > 0);
    assertEquals(0, bytesRef1.compareTo(new BytesRef(data1)));
  }

  @Test
  public void testDeepCopyOf() {
    byte[] data = {1, 2, 3};
    BytesRef original = new BytesRef(data);
    BytesRef copy = BytesRef.deepCopyOf(original);
    assertEquals(original, copy);
    assertNotSame(original.bytes, copy.bytes);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidBytesRef() {
    byte[] data = {1, 2, 3};
    BytesRef bytesRef = new BytesRef(data, 0, 4); // invalid length
    bytesRef.isValid(); // should throw IllegalStateException
  }
}