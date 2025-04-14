/*
 * Copyright (C) 2013 The Guava Authors
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
import java.nio.CharBuffer;
import junit.framework.TestCase;

/**
 * Tests for {@link CharSequenceReader}.
 *
 * <p>TODO(cgdecker): Make this an AbstractReaderTestSuite test.
 *
 * @author Colin Decker
 */
public class CharSequenceReaderTest extends TestCase {

  public void testRead() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("abc");

    assertThat(reader.read()).isEqualTo('a');
    char[] chars = new char[2];
    assertThat(reader.read(chars)).isEqualTo(2);
    assertThat(chars).isEqualTo(new char[] {'b', 'c'});
    assertThat(reader.read()).isEqualTo(-1);
  }

  public void testRead_charBuffer() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("abc");

    CharBuffer buf = CharBuffer.allocate(4);
    assertThat(reader.read(buf)).isEqualTo(3);
    assertThat(reader.read(buf)).isEqualTo(-1);
    assertThat(buf.flip().toString()).isEqualTo("abc");
  }

  public void testSkip() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("abc");

    assertThat(reader.skip(1)).isEqualTo(1);
    assertThat(reader.read()).isEqualTo('b');
    assertThat(reader.skip(2)).isEqualTo(1);
    assertThat(reader.read()).isEqualTo(-1);
    assertThat(reader.skip(1)).isEqualTo(0);
  }

  public void testMarkAndReset() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("abc");

    assertThat(reader.read()).isEqualTo('a');
    reader.mark(0);
    assertThat(reader.read()).isEqualTo('b');
    reader.reset();
    assertThat(reader.read()).isEqualTo('b');
  }

  public void testClose() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("abc");
    reader.close();
    assertThrows(IOException.class, reader::read);
  }

  public void testReadAfterEnd() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("a");
    assertThat(reader.read()).isEqualTo('a');
    assertThat(reader.read()).isEqualTo(-1);
    assertThat(reader.read()).isEqualTo(-1);

    char[] buf = new char[1];
    assertThat(reader.read(buf)).isEqualTo(-1);
    assertThat(reader.read(buf)).isEqualTo(-1);
  }

  public void testSkipAfterEnd() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("a");
    assertThat(reader.skip(1)).isEqualTo(1);
    assertThat(reader.skip(1)).isEqualTo(0);
    assertThat(reader.skip(1)).isEqualTo(0);
  }

  public void testNullCharBuffer() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("a");
    assertThrows(NullPointerException.class, () -> reader.read((CharBuffer) null));
  }

  public void testNegativeSkip() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("a");
    assertThrows(IllegalArgumentException.class, () -> reader.skip(-1));
  }
}