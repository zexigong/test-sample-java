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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 *
 * @author Colin Decker
 */
public class CharSequenceReaderTest {

  private final Reader reader = new CharSequenceReader("abc");

  @Test
  public void read() throws IOException {
    assertEquals('a', reader.read());
    assertEquals('b', reader.read());
    assertEquals('c', reader.read());
    assertEquals(-1, reader.read());
  }

  @Test
  public void readToCharArray() throws IOException {
    char[] buf = new char[4];
    assertEquals(3, reader.read(buf));
    assertEquals('a', buf[0]);
    assertEquals('b', buf[1]);
    assertEquals('c', buf[2]);
    assertEquals(0, buf[3]);
    assertEquals(-1, reader.read(buf));
  }

  @Test
  public void readToCharArray_withOffsetAndLength() throws IOException {
    char[] buf = new char[5];
    assertEquals(3, reader.read(buf, 1, 3));
    assertEquals(0, buf[0]);
    assertEquals('a', buf[1]);
    assertEquals('b', buf[2]);
    assertEquals('c', buf[3]);
    assertEquals(0, buf[4]);
    assertEquals(-1, reader.read(buf, 1, 3));
  }

  @Test
  public void readToCharBuffer() throws IOException {
    CharBuffer buf = CharBuffer.allocate(5);
    assertEquals(3, reader.read(buf));
    buf.flip();
    assertEquals('a', buf.get());
    assertEquals('b', buf.get());
    assertEquals('c', buf.get());
    assertEquals(0, buf.get());
    assertEquals(-1, reader.read(buf));
  }

  @Test
  public void skip() throws IOException {
    assertEquals(2, reader.skip(2));
    assertEquals('c', reader.read());
    assertEquals(-1, reader.read());
  }

  @Test
  public void skipPastEndOfString() throws IOException {
    assertEquals(3, reader.skip(5));
    assertEquals(-1, reader.read());
  }

  @Test
  public void ready() throws IOException {
    assertThat(reader.ready()).isTrue();
    reader.skip(3);
    assertThat(reader.ready()).isTrue();
  }

  @Test
  public void markAndReset() throws IOException {
    reader.skip(1);
    reader.mark(3);
    assertEquals('b', reader.read());
    assertEquals('c', reader.read());
    assertEquals(-1, reader.read());

    reader.reset();
    assertEquals('b', reader.read());
    assertEquals('c', reader.read());
    assertEquals(-1, reader.read());
  }

  @Test
  public void close() throws IOException {
    reader.close();
    try {
      reader.read();
      fail();
    } catch (IOException expected) {
    }
  }

  @Test
  public void negativeSkip() throws IOException {
    try {
      reader.skip(-1);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void negativeMark() throws IOException {
    try {
      reader.mark(-1);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void negativeRead() throws IOException {
    try {
      reader.read(new char[1], 0, -1);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  @Test
  public void negativeOffsetRead() throws IOException {
    try {
      reader.read(new char[1], -1, 1);
      fail();
    } catch (IndexOutOfBoundsException expected) {
    }
  }

  @Test
  public void outOfBoundsRead() throws IOException {
    try {
      reader.read(new char[1], 0, 2);
      fail();
    } catch (IndexOutOfBoundsException expected) {
    }
  }

  @Test
  public void negativeOffsetOutOfBoundsRead() throws IOException {
    try {
      reader.read(new char[1], -1, 2);
      fail();
    } catch (IndexOutOfBoundsException expected) {
    }
  }
}