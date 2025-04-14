/*
 * Copyright (C) 2011 The Guava Authors
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
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import junit.framework.TestCase;

/** Unit tests for {@link ByteSink}. */
@GwtIncompatible // ByteSource, ByteSink
public class ByteSinkTest extends TestCase {

  private static final byte[] bytes = newPreFilledByteArray(10000);

  private static byte[] newPreFilledByteArray(int size) {
    byte[] bytes = new byte[size];
    for (int i = 0; i < size; i++) {
      bytes[i] = (byte) i;
    }
    return bytes;
  }

  private final ByteSink sink = new ByteSink() {
    @Override
    public OutputStream openStream() {
      return out;
    }
  };

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();

  public void testOpenBufferedStream() throws IOException {
    OutputStream bufferedStream = sink.openBufferedStream();
    assertThat(bufferedStream).isNotInstanceOf(ByteArrayOutputStream.class);
    bufferedStream.close();
  }

  public void testWrite() throws IOException {
    sink.write(bytes);
    assertThat(out.toByteArray()).isEqualTo(bytes);
  }

  public void testWriteFrom_inputStream() throws IOException {
    ByteArrayInputStream in = new ByteArrayInputStream(bytes);
    assertThat(sink.writeFrom(in)).isEqualTo(bytes.length);
    assertThat(out.toByteArray()).isEqualTo(bytes);
  }

  public void testWriteFrom_byteSource() throws IOException {
    ByteSource source = ByteSource.wrap(bytes);
    assertThat(source.copyTo(sink)).isEqualTo(bytes.length);
    assertThat(out.toByteArray()).isEqualTo(bytes);
  }

  public void testHashingSink() throws IOException {
    HashCode hash = sink.hash(Hashing.md5()).putBytes(bytes).hash();
    assertThat(hash).isEqualTo(Hashing.md5().hashBytes(bytes));
  }

  public void testAsCharSink() throws IOException {
    String text = "Foo\nBar\n";
    ByteSink.CharSink charSink = sink.asCharSink(UTF_8);
    charSink.write(text);
    assertThat(new String(out.toByteArray(), UTF_8)).isEqualTo(text);
  }
}