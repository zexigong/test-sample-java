/*
 * Copyright (C) 2009 The Guava Authors
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
import java.io.IOException;
import junit.framework.TestCase;

/**
 * Unit tests for {@link LittleEndianDataOutputStream}.
 *
 * <p>Adapted from the tests for {@code java.io.DataOutputStream}.
 */
@GwtIncompatible // LittleEndianDataOutputStream
public class LittleEndianDataOutputStreamTest extends TestCase {
  private static final int SIMULATED_SUN_UTF_LIMIT = 65535;

  private ByteArrayOutputStream bytes;
  private DataOutput out;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    bytes = new ByteArrayOutputStream();
    out = new LittleEndianDataOutputStream(bytes);
  }

  public void testWriteShort() throws IOException {
    out.writeShort(0x1234);
    assertThat(bytes.toByteArray()).isEqualTo(new byte[] {0x34, 0x12});
  }

  public void testWriteChar() throws IOException {
    out.writeChar(0x1234);
    assertThat(bytes.toByteArray()).isEqualTo(new byte[] {0x34, 0x12});
  }

  public void testWriteInt() throws IOException {
    out.writeInt(0x12345678);
    assertThat(bytes.toByteArray()).isEqualTo(new byte[] {0x78, 0x56, 0x34, 0x12});
  }

  public void testWriteLong() throws IOException {
    out.writeLong(0x123456789ABCDEF0L);
    assertThat(bytes.toByteArray())
        .isEqualTo(new byte[] {(byte) 0xF0, (byte) 0xDE, (byte) 0xBC, (byte) 0x9A, 0x78, 0x56, 0x34,
            0x12});
  }

  public void testWriteLong_minValue() throws IOException {
    out.writeLong(Long.MIN_VALUE);
    assertThat(bytes.toByteArray()).isEqualTo(new byte[] {0, 0, 0, 0, 0, 0, 0, (byte) 0x80});
  }

  public void testWriteLong_maxValue() throws IOException {
    out.writeLong(Long.MAX_VALUE);
    assertThat(bytes.toByteArray())
        .isEqualTo(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, 0x7F});
  }

  public void testWriteFloat() throws IOException {
    out.writeFloat(Float.intBitsToFloat(0x12345678));
    assertThat(bytes.toByteArray()).isEqualTo(new byte[] {0x78, 0x56, 0x34, 0x12});
  }

  public void testWriteDouble() throws IOException {
    out.writeDouble(Double.longBitsToDouble(0x123456789ABCDEF0L));
    assertThat(bytes.toByteArray())
        .isEqualTo(new byte[] {(byte) 0xF0, (byte) 0xDE, (byte) 0xBC, (byte) 0x9A, 0x78, 0x56, 0x34,
            0x12});
  }

  public void testWriteBytes() throws IOException {
    out.writeBytes("hello world");
    assertThat(bytes.toByteArray()).isEqualTo("hello world".getBytes(Ascii.CHARSET));
  }

  public void testWriteChars() throws IOException {
    out.writeChars("hello world");
    assertThat(bytes.toByteArray())
        .isEqualTo(
            new byte[] {
              'h', 0,
              'e', 0,
              'l', 0,
              'l', 0,
              'o', 0,
              ' ', 0,
              'w', 0,
              'o', 0,
              'r', 0,
              'l', 0,
              'd', 0
            });
  }

  public void testWriteUTF() throws IOException {
    out.writeUTF("hello world");
    assertThat(bytes.toByteArray())
        .isEqualTo(
            new byte[] {
              0, 11,
              'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'
            });
  }

  public void testClose() throws IOException {
    bytes.close();
    try {
      out.writeInt(0);
      fail();
    } catch (IOException expected) {
    }
  }

  public void testWriteUTF_TooLong() throws IOException {
    StringBuilder builder = new StringBuilder(SIMULATED_SUN_UTF_LIMIT + 1);
    for (int i = 0; i < SIMULATED_SUN_UTF_LIMIT + 1; i++) {
      builder.append('a');
    }
    try {
      out.writeUTF(builder.toString());
      fail();
    } catch (IOException expected) {
    }
  }
}