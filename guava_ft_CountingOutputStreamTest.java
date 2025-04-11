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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.testing.NullPointerTester;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for {@link CountingOutputStream}.
 *
 * @author Chris Nokleberg
 */
@RunWith(JUnit4.class)
public class CountingOutputStreamTest extends IoTestCase {
  @Test
  public void testWrite() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    CountingOutputStream countingOutputStream = new CountingOutputStream(byteArrayOutputStream);
    assertThat(countingOutputStream.getCount()).isEqualTo(0);
    countingOutputStream.write(0);
    assertThat(countingOutputStream.getCount()).isEqualTo(1);
    countingOutputStream.write(new byte[10]);
    assertThat(countingOutputStream.getCount()).isEqualTo(11);
    countingOutputStream.write(new byte[10], 0, 3);
    assertThat(countingOutputStream.getCount()).isEqualTo(14);
    countingOutputStream.write(new byte[10], 1, 4);
    assertThat(countingOutputStream.getCount()).isEqualTo(18);
    countingOutputStream.close();
  }

  @Test
  public void testWriteFails() throws Exception {
    final List<IOException> exceptions =
        ImmutableList.of(
            new IOException("foo"),
            new IOException("bar"),
            new IOException("baz"),
            new IOException("quux"));

    for (final IOException exception : exceptions) {
      OutputStream failingStream =
          new OutputStream() {
            @Override
            public void write(int b) throws IOException {
              throw exception;
            }
          };
      CountingOutputStream countingOutputStream = new CountingOutputStream(failingStream);
      try {
        countingOutputStream.write(1);
        fail();
      } catch (IOException e) {
        assertThat(e).isEqualTo(exception);
      }
    }

    final IOException exception = new IOException("foo");
    OutputStream failingStream =
        new OutputStream() {
          @Override
          public void write(int b) throws IOException {
            throw exception;
          }

          @Override
          public void write(byte[] b, int off, int len) throws IOException {
            throw exception;
          }
        };
    CountingOutputStream countingOutputStream = new CountingOutputStream(failingStream);

    for (int i = 0; i < exceptions.size(); i++) {
      try {
        countingOutputStream.write(new byte[i], 0, i);
        fail();
      } catch (IOException e) {
        assertThat(e).isEqualTo(exception);
      }
    }
  }

  @Test
  public void testNulls() {
    NullPointerTester tester = new NullPointerTester();
    tester.setDefault(byte[].class, new byte[1]);
    tester.setDefault(OutputStream.class, new ByteArrayOutputStream());
    tester.testAllPublicConstructors(CountingOutputStream.class);
    tester.testAllPublicInstanceMethods(
        new CountingOutputStream(new ByteArrayOutputStream()));
  }

  @Test
  public void testFlush() throws IOException {
    final boolean[] called = new boolean[1];
    called[0] = false;

    OutputStream output =
        new OutputStream() {
          @Override
          public void write(int b) throws IOException {}

          @Override
          public void flush() throws IOException {
            called[0] = true;
          }
        };

    CountingOutputStream countingOutputStream = new CountingOutputStream(output);
    countingOutputStream.flush();
    assertTrue(called[0]);
  }

  // Tests that CountingOutputStream.close() does not call flush() on the underlying stream.
  @Test
  public void testCloseDoesNotFlush() throws IOException {
    final boolean[] called = new boolean[1];
    called[0] = false;

    OutputStream output =
        new OutputStream() {
          @Override
          public void write(int b) throws IOException {}

          @Override
          public void flush() throws IOException {
            called[0] = true;
          }
        };

    CountingOutputStream countingOutputStream = new CountingOutputStream(output);
    countingOutputStream.close();
    assertFalse(called[0]);
  }

  @Test
  public void testClose() throws IOException {
    final boolean[] closed = new boolean[1];
    closed[0] = false;

    OutputStream output =
        new OutputStream() {
          @Override
          public void write(int b) {}

          @Override
          public void close() {
            closed[0] = true;
          }
        };

    CountingOutputStream countingOutputStream = new CountingOutputStream(output);
    assertFalse(closed[0]);
    countingOutputStream.close();
    assertTrue(closed[0]);
  }

  @Test
  public void testCloseFails() throws IOException {
    final IOException exception = new IOException("foo");

    OutputStream output =
        new OutputStream() {
          @Override
          public void write(int b) throws IOException {}

          @Override
          public void close() throws IOException {
            throw exception;
          }
        };

    CountingOutputStream countingOutputStream = new CountingOutputStream(output);
    try {
      countingOutputStream.close();
      fail();
    } catch (IOException e) {
      assertThat(e).isEqualTo(exception);
    }
  }
}