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

import static org.apache.lucene.util.RamUsageTester.sizeOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import org.apache.lucene.tests.util.LuceneTestCase;

public class TestCharsRefBuilder extends LuceneTestCase {

  public void testAppend() {
    CharsRefBuilder ref = new CharsRefBuilder();
    String[] randomStrings = new String[random().nextInt(8) + 1];
    for (int i = 0; i < randomStrings.length; i++) {
      randomStrings[i] = TestUtil.randomRealisticUnicodeString(random());
      ref.append(randomStrings[i]);
    }
    StringBuilder builder = new StringBuilder();
    for (String string : randomStrings) {
      builder.append(string);
    }
    assertEquals(builder.toString(), ref.toString());
  }

  public void testCopy() {
    CharsRefBuilder ref = new CharsRefBuilder();
    String string = TestUtil.randomRealisticUnicodeString(random());
    ref.copyChars(string);
    assertEquals(string, ref.toString());
    ref.copyChars(new char[] {'a', 'b', 'c'}, 0, 3);
    assertEquals("abc", ref.toString());
  }

  public void testUTF8Conversion() {
    int numValues = atLeast(1000);
    for (int i = 0; i < numValues; i++) {
      String unicode = TestUtil.randomUnicodeString(random());
      CharsRefBuilder ref = new CharsRefBuilder();
      ref.copyUTF8Bytes(new BytesRef(unicode));
      assertEquals(unicode, ref.toString());
    }
  }

  public void testAppendNullString() {
    CharsRefBuilder ref = new CharsRefBuilder();
    ref.append((String) null);
    assertEquals("null", ref.toString());
  }

  public void testAppendNullCharSequence() {
    CharsRefBuilder ref = new CharsRefBuilder();
    ref.append((CharSequence) null);
    assertEquals("null", ref.toString());
  }

  public void testAppendCharSequence() {
    CharsRefBuilder ref = new CharsRefBuilder();
    ref.append("foobar", 3, 6);
    assertEquals("bar", ref.toString());
  }

  public void testAppendCharArray() {
    CharsRefBuilder ref = new CharsRefBuilder();
    ref.append(new char[] {'f', 'o', 'o', 'b', 'a', 'r'}, 3, 3);
    assertEquals("bar", ref.toString());
  }

  public void testAppendCharsRef() {
    CharsRefBuilder ref = new CharsRefBuilder();
    ref.append(new CharsRef("foobar"), 3, 3);
    assertEquals("bar", ref.toString());
  }

  public void testToCharsRef() {
    CharsRefBuilder ref = new CharsRefBuilder();
    ref.append("foobar", 3, 3);
    CharsRef copy = ref.toCharsRef();
    ref.append("baz");
    assertEquals("bar", copy.toString());
  }

  public void testGet() {
    CharsRefBuilder ref = new CharsRefBuilder();
    ref.append("foobar", 3, 3);
    CharsRef copy = ref.get();
    ref.append("baz");
    assertEquals("barbaz", copy.toString());
  }

  public void testCopyBytes() {
    CharsRefBuilder ref = new CharsRefBuilder();
    BytesRef bytes = new BytesRef("foobar");
    ref.copyUTF8Bytes(bytes, 3, 3);
    assertEquals("bar", ref.toString());
  }

  public void testCapacity() {
    CharsRefBuilder ref = new CharsRefBuilder();
    ref.grow(5);
    long size = sizeOf(ref.chars());
    ref.grow(100);
    assertThat(sizeOf(ref.chars()), greaterThan(size));
    ref.grow(5);
    assertThat(sizeOf(ref.chars()), equalTo(sizeOf(ref.chars())));
  }
}