/*
 * Copyright (C) 2008 The Guava Authors
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

import com.google.common.annotations.GwtIncompatible;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.junit.Test;

/**
 * Unit test for {@link LittleEndianDataOutputStream}.
 *
 * @author Chris Nokleberg
 */
@GwtIncompatible
public class LittleEndianDataOutputStreamTest extends IoTestCase {
  // This test uses an in-memory byte array as the underlying output stream to
  // LittleEndianDataOutputStream. It writes various numbers to the stream, then
  // compares the result to the expected sequence of bytes.

  @Test
  public void testWrite() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    LittleEndianDataOutputStream out = new LittleEndianDataOutputStream(baos);
    // We use the high bit to detect errors in the first byte written below.
    byte[] expected = new byte[] {
      (byte) 0x80, 0, (byte) 0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    };

    out.writeByte(0x80);
    out.writeShort(0x80);
    out.writeChar(0x80);
    out.writeInt(0x80);
    out.writeLong(0x80);

    assertThat(baos.toByteArray()).isEqualTo(expected);
  }

  @Test
  public void testWritePrimitiveArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    LittleEndianDataOutputStream out = new LittleEndianDataOutputStream(baos);

    byte[] data = {1, 2, 3, 4, 5, 6};
    out.write(data);

    assertThat(baos.toByteArray()).isEqualTo(data);
  }

  @Test
  public void testFlush() throws IOException {
    final boolean[] wasFlushed = {false};
    LittleEndianDataOutputStream out =
        new LittleEndianDataOutputStream(
            new FilterOutputStream(new ByteArrayOutputStream()) {
              @Override
              public void flush() {
                wasFlushed[0] = true;
              }
            });
    out.flush();
    assertThat(wasFlushed[0]).isTrue();
  }

  @Test
  public void testClose() throws IOException {
    final boolean[] wasClosed = {false};
    LittleEndianDataOutputStream out =
        new LittleEndianDataOutputStream(
            new FilterOutputStream(new ByteArrayOutputStream()) {
              @Override
              public void close() {
                wasClosed[0] = true;
              }
            });
    out.close();
    assertThat(wasClosed[0]).isTrue();
  }

  @Test
  public void testWriteBytes_throws() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    LittleEndianDataOutputStream out = new LittleEndianDataOutputStream(baos);

    assertThrows(IllegalArgumentException.class, () -> out.writeBytes("foo"));
  }
}