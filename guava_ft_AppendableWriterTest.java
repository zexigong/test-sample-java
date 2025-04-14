/*
 * Copyright (C) 2006 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.io;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for {@link AppendableWriter}.
 *
 * @author Alan Green
 */
@RunWith(JUnit4.class)
public class AppendableWriterTest {

  private final StringBuilder target = new StringBuilder();
  private final Writer writer = new AppendableWriter(target);

  @Test
  public void testWriteChar() throws IOException {
    writer.write('a');
    assertThat(target.toString()).isEqualTo("a");
    writer.write('b');
    assertThat(target.toString()).isEqualTo("ab");
  }

  @Test
  public void testWritePortionOfCharArray() throws IOException {
    char[] chars = "abcd".toCharArray();
    writer.write(chars, 1, 2);
    assertThat(target.toString()).isEqualTo("bc");
  }

  @Test
  public void testWritePortionOfString() throws IOException {
    writer.write("abcd", 1, 2);
    assertThat(target.toString()).isEqualTo("bc");
  }

  @Test
  public void testWriteString() throws IOException {
    writer.write("abcd");
    assertThat(target.toString()).isEqualTo("abcd");
  }

  @Test
  public void testWriteNullString() throws IOException {
    assertThrows(NullPointerException.class, () -> writer.write((String) null));
  }

  @Test
  public void testWriteNullStringWithIndexes() throws IOException {
    assertThrows(NullPointerException.class, () -> writer.write(null, 0, 1));
  }

  @Test
  public void testAppendChar() throws IOException {
    writer.append('a');
    assertThat(target.toString()).isEqualTo("a");
    writer.append('b').append('c');
    assertThat(target.toString()).isEqualTo("abc");
  }

  @Test
  public void testAppendCharSequence() throws IOException {
    writer.append("a");
    assertThat(target.toString()).isEqualTo("a");
    writer.append("bc").append("de");
    assertThat(target.toString()).isEqualTo("abcde");
  }

  @Test
  public void testAppendPortionOfCharSequence() throws IOException {
    writer.append("abcd", 1, 3);
    assertThat(target.toString()).isEqualTo("bc");
  }

  @Test
  public void testAppendNullCharSequence() throws IOException {
    writer.append((CharSequence) null);
    assertThat(target.toString()).isEqualTo("null");
  }

  @Test
  public void testAppendNullCharSequenceWithIndexes() throws IOException {
    writer.append(null, 1, 2);
    assertThat(target.toString()).isEqualTo("u");
  }

  @Test
  public void testWriterClose() throws IOException {
    List<String> data = new ArrayList<>();
    try (Writer writer = new AppendableWriter(data)) {
      writer.write("foo");
    }
    assertThat(data).containsExactly("foo");
  }
}