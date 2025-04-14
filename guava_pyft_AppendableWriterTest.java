/*
 * Copyright (C) 2012 The Guava Authors
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
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.fail;

import com.google.common.testing.NullPointerTester;
import com.google.common.testing.NullPointerTester.Visibility;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

/** Unit test for {@link AppendableWriter}. */
public class AppendableWriterTest {
  @Test
  public void testAppendChar() throws IOException {
    StringBuilder builder = new StringBuilder();
    AppendableWriter writer = new AppendableWriter(builder);
    writer.append('x');
    writer.append('y').append('z');
    assertThat(builder.toString()).isEqualTo("xyz");
    writer.close();
  }

  @Test
  public void testAppendCharSequence() throws IOException {
    StringBuilder builder = new StringBuilder();
    AppendableWriter writer = new AppendableWriter(builder);
    writer.append("xy");
    writer.append("z");
    assertThat(builder.toString()).isEqualTo("xyz");
    writer.close();
  }

  @Test
  public void testAppendCharSequencePortion() throws IOException {
    StringBuilder builder = new StringBuilder();
    AppendableWriter writer = new AppendableWriter(builder);
    writer.append("abcd", 1, 3);
    writer.append("abcd", 2, 4);
    assertThat(builder.toString()).isEqualTo("bcdbcd");
    writer.close();
  }

  @Test
  public void testWriteChar() throws IOException {
    StringBuilder builder = new StringBuilder();
    AppendableWriter writer = new AppendableWriter(builder);
    writer.write('x');
    writer.write('y');
    writer.write('z');
    assertThat(builder.toString()).isEqualTo("xyz");
    writer.close();
  }

  @Test
  public void testWriteCharArray() throws IOException {
    StringBuilder builder = new StringBuilder();
    AppendableWriter writer = new AppendableWriter(builder);
    writer.write("abcd".toCharArray());
    writer.write("efgh".toCharArray(), 1, 2);
    assertThat(builder.toString()).isEqualTo("abcdefgh");
    writer.close();
  }

  @Test
  public void testWriteString() throws IOException {
    StringBuilder builder = new StringBuilder();
    AppendableWriter writer = new AppendableWriter(builder);
    writer.write("abcd");
    writer.write("efgh", 1, 2);
    assertThat(builder.toString()).isEqualTo("abcdefgh");
    writer.close();
  }

  @Test
  public void testClose() throws IOException {
    StringBuilder builder = new StringBuilder();
    AppendableWriter writer = new AppendableWriter(builder);
    writer.write("abcd");
    writer.close();
    assertThat(builder.toString()).isEqualTo("abcd");
    try {
      writer.write("x");
      fail();
    } catch (IOException expected) {
    }
    try {
      writer.append('x');
      fail();
    } catch (IOException expected) {
    }
    try {
      writer.append("xyz");
      fail();
    } catch (IOException expected) {
    }
    try {
      writer.append("xyz", 1, 2);
      fail();
    } catch (IOException expected) {
    }
    try {
      writer.write("xyz", 1, 2);
      fail();
    } catch (IOException expected) {
    }
  }

  @Test
  public void testCloseCloseable() throws IOException {
    final boolean[] closed = {false};
    Appendable closeable =
        new StringBuilder() {
          @Override
          public void finalize() {
            closed[0] = true;
          }
        };
    AppendableWriter writer = new AppendableWriter(closeable);
    assertThat(closed[0]).isFalse();
    writer.close();
    assertThat(closed[0]).isTrue();
  }

  @Test
  public void testFlush() throws IOException {
    final boolean[] flushed = {false};
    Appendable flushable =
        new StringBuilder() {
          @Override
          public void finalize() {
            flushed[0] = true;
          }
        };
    AppendableWriter writer = new AppendableWriter(flushable);
    assertThat(flushed[0]).isFalse();
    writer.flush();
    assertThat(flushed[0]).isTrue();
  }

  @Test
  public void testNulls() {
    new NullPointerTester()
        .setDefault(char[].class, new char[] {})
        .testConstructors(AppendableWriter.class, Visibility.PACKAGE);
  }
}