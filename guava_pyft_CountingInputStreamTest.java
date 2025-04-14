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
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableSet;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link CountingInputStream}. */
@RunWith(JUnit4.class)
public class CountingInputStreamTest {
  private static final byte[] DATA = newPreFilledByteArray(10000);

  @Test
  public void testRead() throws IOException {
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(DATA));
    assertThat(in.getCount()).isEqualTo(0);
    in.read();
    assertThat(in.getCount()).isEqualTo(1);
    in.read(new byte[10]);
    assertThat(in.getCount()).isEqualTo(11);
    in.read(new byte[10], 0, 5);
    assertThat(in.getCount()).isEqualTo(16);
    in.skip(100);
    assertThat(in.getCount()).isEqualTo(116);
    in.read(new byte[10], 0, 0);
    assertThat(in.getCount()).isEqualTo(116);
    in.skip(0);
    assertThat(in.getCount()).isEqualTo(116);
  }

  @Test
  public void testMarkReset() throws IOException {
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(DATA));
    Set<Integer> expectedValues = new HashSet<>(Arrays.asList(0, 1, 11, 16, 116, 16, 116));
    ImmutableSet.Builder<Integer> actualValues = ImmutableSet.builder();
    actualValues.add((int) in.getCount());
    in.read();
    actualValues.add((int) in.getCount());
    in.read(new byte[10]);
    actualValues.add((int) in.getCount());
    in.read(new byte[10], 0, 5);
    actualValues.add((int) in.getCount());
    in.mark(0);
    in.skip(100);
    actualValues.add((int) in.getCount());
    in.reset();
    actualValues.add((int) in.getCount());
    in.skip(100);
    actualValues.add((int) in.getCount());
    assertThat(actualValues.build()).isEqualTo(expectedValues);
  }

  @Test
  public void testMarkReset_noMarkSet() throws IOException {
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(DATA));
    in.read();
    IOException expected =
        assertThrows(
            IOException.class,
            new ThrowingRunnable() {
              @Override
              public void run() throws Throwable {
                in.reset();
              }
            });
    assertThat(expected).hasMessageThat().contains("Mark not set");
  }

  @Test
  public void testMarkReset_markNotSupported() throws IOException {
    InputStream in =
        new InputStream() {
          @Override
          public int read() {
            return -1;
          }
        };
    CountingInputStream countingIn = new CountingInputStream(in);
    countingIn.mark(10); // should not throw
    IOException expected =
        assertThrows(
            IOException.class,
            new ThrowingRunnable() {
              @Override
              public void run() throws Throwable {
                countingIn.reset();
              }
            });
    assertThat(expected).hasMessageThat().contains("Mark not supported");
  }

  @Test
  public void testSkip() throws IOException {
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(DATA));
    assertThat(in.getCount()).isEqualTo(0);
    in.skip(1);
    assertThat(in.getCount()).isEqualTo(1);
    in.skip(10);
    assertThat(in.getCount()).isEqualTo(11);
    in.skip(5);
    assertThat(in.getCount()).isEqualTo(16);
    in.skip(100);
    assertThat(in.getCount()).isEqualTo(116);
    in.skip(0);
    assertThat(in.getCount()).isEqualTo(116);
  }

  private static byte[] newPreFilledByteArray(int size) {
    byte[] array = new byte[size];
    for (int i = 0; i < size; i++) {
      array[i] = (byte) i;
    }
    return array;
  }
}