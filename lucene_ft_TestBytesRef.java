/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.util;

import java.util.Random;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.junit.Test;

public class TestBytesRef extends LuceneTestCase {

  private Random random = random();

  @Test
  public void testEmpty() {
    BytesRef b = new BytesRef();
    assertEquals(0, b.length);
    assertEquals(BytesRef.EMPTY_BYTES, b.bytes);
  }

  @Test
  public void testFromBytes() {
    BytesRef b = new BytesRef(new byte[] {1, 2, 3, 4});
    assertEquals(4, b.length);
    assertEquals(0, b.offset);
    assertEquals(4, b.bytes.length);
  }

  @Test
  public void testCopyBytes() {
    BytesRef b = new BytesRef(new byte[] {1, 2, 3, 4}, 1, 3);
    assertEquals(3, b.length);
    assertEquals(1, b.offset);
    assertEquals(4, b.bytes.length);
  }

  @Test
  public void testFromString() {
    BytesRef b = new BytesRef("abcd");
    assertEquals(4, b.length);
    assertEquals(0, b.offset);
    assertEquals(4, b.bytes.length);
  }

  @Test
  public void testAppendBytes() {
    BytesRef b = new BytesRef("abcd");
    b = BytesRef.deepCopyOf(b);
    BytesRef.append(b, new byte[] {1, 2, 3, 4});
    assertEquals(8, b.length);
    assertEquals(0, b.offset);
    assertEquals(8, b.bytes.length);
    assertEquals(new BytesRef("abcd\u0001\u0002\u0003\u0004"), b);
  }

  @Test
  public void testAppendBytesRef() {
    BytesRef b = new BytesRef("abcd");
    b = BytesRef.deepCopyOf(b);
    BytesRef.append(b, new BytesRef(new byte[] {1, 2, 3, 4}));
    assertEquals(8, b.length);
    assertEquals(0, b.offset);
    assertEquals(8, b.bytes.length);
    assertEquals(new BytesRef("abcd\u0001\u0002\u0003\u0004"), b);
  }

  @Test
  public void testAppendString() {
    BytesRef b = new BytesRef("abcd");
    b = BytesRef.deepCopyOf(b);
    BytesRef.append(b, "efgh");
    assertEquals(8, b.length);
    assertEquals(0, b.offset);
    assertEquals(8, b.bytes.length);
    assertEquals(new BytesRef("abcdefgh"), b);
  }

  @Test
  public void testAppendChar() {
    BytesRef b = new BytesRef("abcd");
    b = BytesRef.deepCopyOf(b);
    BytesRef.append(b, (char) 0x61);
    assertEquals(5, b.length);
    assertEquals(0, b.offset);
    assertEquals(5, b.bytes.length);
    assertEquals(new BytesRef("abcde"), b);
  }

  @Test
  public void testAppendInt() {
    BytesRef b = new BytesRef("abcd");
    b = BytesRef.deepCopyOf(b);
    BytesRef.append(b, 1);
    assertEquals(5, b.length);
    assertEquals(0, b.offset);
    assertEquals(5, b.bytes.length);
    assertEquals(new BytesRef("abcd1"), b);
  }

  @Test
  public void testHashCode() {
    BytesRef b = new BytesRef("abcd");
    assertEquals(b.hashCode(), b.hashCode());
    assertNotEquals(b.hashCode(), new BytesRef("abce").hashCode());
  }

  @Test
  public void testEquals() {
    BytesRef b = new BytesRef("abcd");
    assertEquals(b, b);
    assertEquals(b, new BytesRef("abcd"));
    assertNotEquals(b, new BytesRef("abce"));
    assertNotEquals(b, null);
    assertNotEquals(b, new Object());
  }

  @Test
  public void testDeepCopyOf() {
    BytesRef b = new BytesRef("abcd");
    BytesRef c = BytesRef.deepCopyOf(b);
    assertEquals(b, c);
    b.bytes[0] = 0;
    assertNotEquals(b, c);
  }

  @Test
  public void testClone() {
    BytesRef b = new BytesRef("abcd");
    BytesRef c = b.clone();
    assertEquals(b, c);
    b.bytes[0] = 0;
    assertEquals(b, c);
  }

  @Test
  public void testUTF8ToString() {
    BytesRef b = new BytesRef("abcd");
    assertEquals("abcd", b.utf8ToString());
  }

  @Test
  public void testCompareTo() {
    BytesRef b = new BytesRef("abcd");
    assertEquals(0, b.compareTo(b));
    assertEquals(0, b.compareTo(new BytesRef("abcd")));
    assertTrue(b.compareTo(new BytesRef("abce")) < 0);
    assertTrue(b.compareTo(new BytesRef("abcc")) > 0);
  }

  @Test
  public void testIsValid() {
    BytesRef b = new BytesRef("abcd");
    assertTrue(b.isValid());
    b.length = -1;
    assertFalse(b.isValid());
    b.length = 5;
    assertFalse(b.isValid());
    b.length = 4;
    b.offset = -1;
    assertFalse(b.isValid());
    b.offset = 5;
    assertFalse(b.isValid());
    b.offset = 0;
    assertTrue(b.isValid());
  }

  @Test
  public void testBytesEquals() {
    BytesRef b = new BytesRef("abcd");
    assertTrue(b.bytesEquals(b));
    assertTrue(b.bytesEquals(new BytesRef("abcd")));
    assertFalse(b.bytesEquals(new BytesRef("abce")));
  }
}