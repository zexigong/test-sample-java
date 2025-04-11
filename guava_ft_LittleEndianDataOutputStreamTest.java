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
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

/**
 * Unit test for {@link LittleEndianDataInputStream}.
 *
 * @author Chris Nokleberg
 */
public class LittleEndianDataOutputStreamTest {
  private static final byte[] TEST_ARRAY =
      new byte[] {
        0, 1, (byte) 0xFF, (byte) 0xFE, (byte) 0x80, 0x7F, (byte) 0x81, (byte) 0xFF, 0x40, 0x20,
        0x10, 0x08, 0x04, 0x02, 0x01, 0x00, (byte) 0xC0, (byte) 0xE0, (byte) 0xF0, (byte) 0xF8,
        (byte) 0xFC, (byte) 0xFE, (byte) 0xFF, (byte) 0xFF
      };

  private static final byte[] HALF_ARRAY = Arrays.copyOf(TEST_ARRAY, TEST_ARRAY.length / 2);

  @Test
  public void testWriteShort() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    LittleEndianDataOutputStream leOut = new LittleEndianDataOutputStream(out);
    for (int i = 0; i < HALF_ARRAY.length; i += 2) {
      leOut.writeShort((HALF_ARRAY[i] & 0xFF) | (HALF_ARRAY[i + 1] << 8));
    }
    leOut.flush();
    byte[] b = out.toByteArray();
    assertThat(b).isEqualTo(HALF_ARRAY);
  }

  @Test
  public void testWriteChar() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    LittleEndianDataOutputStream leOut = new LittleEndianDataOutputStream(out);
    for (int i = 0; i < HALF_ARRAY.length; i += 2) {
      leOut.writeChar((HALF_ARRAY[i] & 0xFF) | (HALF_ARRAY[i + 1] << 8));
    }
    leOut.flush();
    byte[] b = out.toByteArray();
    assertThat(b).isEqualTo(HALF_ARRAY);
  }

  @Test
  public void testWriteInt() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    LittleEndianDataOutputStream leOut = new LittleEndianDataOutputStream(out);
    for (int i = 0; i < TEST_ARRAY.length; i += 4) {
      leOut.writeInt(
          (TEST_ARRAY[i] & 0xFF)
              | (TEST_ARRAY[i + 1] & 0xFF) << 8
              | (TEST_ARRAY[i + 2] & 0xFF) << 16
              | (TEST_ARRAY[i + 3] & 0xFF) << 24);
    }
    leOut.flush();
    byte[] b = out.toByteArray();
    assertThat(b).isEqualTo(TEST_ARRAY);
  }

  @Test
  public void testWriteLong() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    LittleEndianDataOutputStream leOut = new LittleEndianDataOutputStream(out);
    leOut.writeLong(0x00010203_04050607L);
    leOut.flush();
    byte[] b = out.toByteArray();
    assertThat(b)
        .isEqualTo(new byte[] {0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00});
  }

  @Test
  public void testWriteDouble() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    LittleEndianDataOutputStream leOut = new LittleEndianDataOutputStream(out);
    leOut.writeDouble(Double.longBitsToDouble(0x0123456789ABCDEFL));
    leOut.flush();
    byte[] b = out.toByteArray();
    assertThat(b)
        .isEqualTo(new byte[] {(byte) 0xEF, (byte) 0xCD, (byte) 0xAB, (byte) 0x89, 0x67, 0x45, 0x23, 0x01});
  }

  @Test
  public void testWriteFloat() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    LittleEndianDataOutputStream leOut = new LittleEndianDataOutputStream(out);
    leOut.writeFloat(Float.intBitsToFloat(0x89ABCDEFL));
    leOut.flush();
    byte[] b = out.toByteArray();
    assertThat(b).isEqualTo(new byte[] {(byte) 0xEF, (byte) 0xCD, (byte) 0xAB, (byte) 0x89});
  }

  @Test
  public void testWriteUTF() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    LittleEndianDataOutputStream leOut = new LittleEndianDataOutputStream(out);
    leOut.writeUTF("Hello world");
    leOut.flush();
    byte[] b = out.toByteArray();
    assertThat(b)
        .isEqualTo(new byte[] {0x00, 0x0B, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64});
  }
}