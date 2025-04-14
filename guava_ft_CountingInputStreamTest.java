/*
 * Copyright (C) 2007 The Guava Authors
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

/**
 * Unit test for {@link CountingInputStream}.
 *
 * @author Chris Nokleberg
 */
public class CountingInputStreamTest {

  private static final byte[] DATA = newPreFilledByteArray(10000);

  @Test
  public void testRead() throws IOException {
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(DATA));
    assertEquals(0, in.getCount());
    assertEquals(DATA[0], in.read());
    assertEquals(1, in.getCount());
    assertEquals(DATA[1], in.read());
    assertEquals(2, in.getCount());
    assertEquals(DATA[2], in.read());
    assertEquals(3, in.getCount());

    assertEquals(4, in.read(new byte[4]));
    assertEquals(7, in.getCount());
    assertEquals(4, in.read(new byte[1000]));
    assertEquals(11, in.getCount());
    assertEquals(1000, in.read(new byte[1000]));
    assertEquals(1011, in.getCount());

    assertEquals(0, in.read(new byte[0]));
    assertEquals(1011, in.getCount());

    assertEquals(8989, ByteStreams.exhaust(in));
    assertEquals(10000, in.getCount());
    assertEquals(-1, in.read());
    assertEquals(10000, in.getCount());
  }

  @Test
  public void testSkip() throws IOException {
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(DATA));
    assertEquals(0, in.getCount());
    assertEquals(100, in.skip(100));
    assertEquals(100, in.getCount());
    assertEquals(DATA[100], in.read());
    assertEquals(101, in.getCount());
    assertEquals(9900, in.skip(10000));
    assertEquals(10001, in.getCount());
  }

  @Test
  public void testMarkAndReset() throws IOException {
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(DATA));
    assertThat(in.markSupported()).isTrue();
    assertEquals(DATA[0], in.read());
    assertEquals(1, in.getCount());
    in.mark(10);
    assertEquals(DATA[1], in.read());
    assertEquals(DATA[2], in.read());
    assertEquals(3, in.getCount());
    in.reset();
    assertEquals(1, in.getCount());
    assertEquals(DATA[1], in.read());
    assertEquals(DATA[2], in.read());
    assertEquals(3, in.getCount());
  }

  @Test
  public void testMarkAndReset_noMark() throws IOException {
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(DATA));
    assertThat(in.markSupported()).isTrue();
    assertEquals(DATA[0], in.read());
    assertEquals(1, in.getCount());
    in.reset();
    assertEquals(1, in.getCount());
    assertEquals(DATA[1], in.read());
    assertEquals(2, in.getCount());
  }

  @Test
  public void testMarkAndReset_markNotSupported() throws IOException {
    CountingInputStream in =
        new CountingInputStream(
            new FilterInputStream(new ByteArrayInputStream(DATA)) {
              @Override
              public boolean markSupported() {
                return false;
              }
            });
    assertThat(in.markSupported()).isFalse();
    assertEquals(DATA[0], in.read());
    assertEquals(1, in.getCount());
    in.mark(10);
    assertEquals(DATA[1], in.read());
    assertEquals(DATA[2], in.read());
    assertEquals(3, in.getCount());
    IOException resetNotSupported = assertThrows(IOException.class, new ThrowingRunnable() {
      @Override
      public void run() throws Throwable {
        in.reset();
      }
    });
    assertThat(resetNotSupported).hasMessageThat().isEqualTo("Mark not supported");
  }

  @Test
  public void testMarkAndReset_resetWithoutMark() throws IOException {
    CountingInputStream in =
        new CountingInputStream(
            new FilterInputStream(new ByteArrayInputStream(DATA)) {
              @Override
              public boolean markSupported() {
                return true;
              }
            });
    assertThat(in.markSupported()).isTrue();
    assertEquals(DATA[0], in.read());
    assertEquals(1, in.getCount());
    IOException resetWithoutMark = assertThrows(IOException.class, new ThrowingRunnable() {
      @Override
      public void run() throws Throwable {
        in.reset();
      }
    });
    assertThat(resetWithoutMark).hasMessageThat().isEqualTo("Mark not set");
  }

  private static byte[] newPreFilledByteArray(int size) {
    byte[] data = new byte[size];
    for (int i = 0; i < size; i++) {
      data[i] = (byte) i;
    }
    return data;
  }

  @Test
  public void testSkipZero() throws IOException {
    InputStream in = new InputStream() {
      @Override
      public int read() {
        return 0;
      }

      @Override
      public long skip(long n) {
        return 0;
      }
    };
    CountingInputStream countingIn = new CountingInputStream(in);
    assertEquals(0, countingIn.skip(0));
    assertEquals(0, countingIn.getCount());
  }
}