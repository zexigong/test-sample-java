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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import junit.framework.TestCase;

/**
 * Unit test for {@link CountingInputStream}.
 *
 * @author Chris Nokleberg
 */
public class CountingInputStreamTest extends TestCase {
  public void testRead() throws IOException {
    byte[] data = newPreFilledByteArray(10000);
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(data));
    assertThat(in.getCount()).isEqualTo(0);
    for (int i = 0; i < 500; i++) {
      assertThat(in.read()).isEqualTo(i % 256);
    }
    assertThat(in.getCount()).isEqualTo(500);
    byte[] buffer = new byte[1000];
    assertThat(in.read(buffer, 0, buffer.length)).isEqualTo(buffer.length);
    assertThat(in.getCount()).isEqualTo(1500);
    assertThat(in.skip(5)).isEqualTo(5);
    assertThat(in.getCount()).isEqualTo(1505);
    assertThat(in.skip(0)).isEqualTo(0);
    assertThat(in.getCount()).isEqualTo(1505);
    assertThat(in.skip(1)).isEqualTo(1);
    assertThat(in.getCount()).isEqualTo(1506);
    assertThat(in.skip(0)).isEqualTo(0);
    assertThat(in.getCount()).isEqualTo(1506);
    assertThat(in.skip(4994)).isEqualTo(4994);
    assertThat(in.getCount()).isEqualTo(6500);
    assertThat(in.skip(1)).isEqualTo(1);
    assertThat(in.getCount()).isEqualTo(6501);
    assertThat(in.skip(3499)).isEqualTo(3499);
    assertThat(in.getCount()).isEqualTo(10000);
    assertThat(in.read()).isEqualTo(-1);
    assertThat(in.getCount()).isEqualTo(10000);
    assertThat(in.skip(1)).isEqualTo(0);
    assertThat(in.getCount()).isEqualTo(10000);
    assertThat(in.read(buffer, 0, 1)).isEqualTo(-1);
    assertThat(in.getCount()).isEqualTo(10000);
  }

  public void testMarkAndReset() throws IOException {
    byte[] data = newPreFilledByteArray(1000);
    CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(data));
    assertThat(in.getCount()).isEqualTo(0);
    for (int i = 0; i < 100; i++) {
      assertThat(in.read()).isEqualTo(i % 256);
    }
    assertThat(in.getCount()).isEqualTo(100);
    in.mark(1000);
    assertThat(in.getCount()).isEqualTo(100);
    for (int i = 100; i < 200; i++) {
      assertThat(in.read()).isEqualTo(i % 256);
    }
    assertThat(in.getCount()).isEqualTo(200);
    in.reset();
    assertThat(in.getCount()).isEqualTo(100);
    for (int i = 100; i < 200; i++) {
      assertThat(in.read()).isEqualTo(i % 256);
    }
    assertThat(in.getCount()).isEqualTo(200);
    in.mark(1000);
    in.skip(100);
    assertThat(in.getCount()).isEqualTo(300);
    in.reset();
    assertThat(in.getCount()).isEqualTo(200);
    in.mark(1000);
    assertThat(in.getCount()).isEqualTo(200);
    for (int i = 0; i < 100; i++) {
      assertThat(in.read()).isEqualTo((200 + i) % 256);
    }
    assertThat(in.getCount()).isEqualTo(300);
    in.reset();
    assertThat(in.getCount()).isEqualTo(200);
    in.mark(1000);
    assertThat(in.getCount()).isEqualTo(200);
    for (int i = 0; i < 300; i++) {
      assertThat(in.read()).isEqualTo((200 + i) % 256);
    }
    assertThat(in.getCount()).isEqualTo(500);
    in.reset();
    assertThat(in.getCount()).isEqualTo(200);
  }

  private byte[] newPreFilledByteArray(int size) {
    byte[] data = new byte[size];
    for (int i = 0; i < size; i++) {
      data[i] = (byte) (i % 256);
    }
    return data;
  }
}