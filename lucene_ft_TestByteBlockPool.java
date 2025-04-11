/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.util;

import java.util.Random;
import org.junit.Test;

/** Unit tests for {@link ByteBlockPool}. */
public class TestByteBlockPool extends LuceneTestCase {

  @Test
  public void testAppend() {
    ByteBlockPool pool = new ByteBlockPool(new ByteBlockPool.DirectAllocator());
    pool.nextBuffer();
    Random r = random();
    final int numValues = atLeast(50000);
    byte[][] data = new byte[numValues][];
    final int maxLength = 30;
    for (int i = 0; i < numValues; i++) {
      final byte[] bytes = new byte[TestUtil.nextInt(r, 1, maxLength)];
      for (int j = 0; j < bytes.length; j++) {
        bytes[j] = (byte) r.nextInt(256);
      }
      data[i] = bytes;
      pool.append(bytes, 0, bytes.length);
    }

    long position = 0;
    for (int i = 0; i < data.length; i++) {
      byte[] bytes = data[i];
      for (int j = 0; j < bytes.length; j++) {
        assertEquals(bytes[j], pool.readByte(position++));
      }
    }
  }

  @Test
  public void testAppendSlice() {
    ByteBlockPool pool = new ByteBlockPool(new ByteBlockPool.DirectAllocator());
    pool.nextBuffer();
    ByteBlockPool src = new ByteBlockPool(new ByteBlockPool.DirectAllocator());
    src.nextBuffer();
    Random r = random();
    final int numValues = atLeast(50000);
    byte[][] data = new byte[numValues][];
    final int maxLength = 30;
    for (int i = 0; i < numValues; i++) {
      final byte[] bytes = new byte[TestUtil.nextInt(r, 1, maxLength)];
      for (int j = 0; j < bytes.length; j++) {
        bytes[j] = (byte) r.nextInt(256);
      }
      data[i] = bytes;
      src.append(bytes, 0, bytes.length);
    }

    long position = 0;
    long offset = 0;
    for (int i = 0; i < data.length; i++) {
      byte[] bytes = data[i];
      pool.append(src, offset, bytes.length);
      for (int j = 0; j < bytes.length; j++) {
        assertEquals(bytes[j], pool.readByte(position++));
      }
      offset += bytes.length;
    }
  }

  @Test
  public void testBytesRefIterator() {
    final int numValues = atLeast(50000);
    final int maxLength = 30;
    final Random r = random();

    final BytesRefIterator iter = new BytesRefIterator(r, numValues, maxLength);
    final byte[][] data = iter.getData();
    final ByteBlockPool pool = new ByteBlockPool(new ByteBlockPool.DirectAllocator());
    pool.nextBuffer();
    BytesRef ref;
    for (int i = 0; (ref = iter.next()) != null; i++) {
      pool.append(ref);
    }

    long position = 0;
    for (int i = 0; i < data.length; i++) {
      byte[] bytes = data[i];
      for (int j = 0; j < bytes.length; j++) {
        assertEquals(bytes[j], pool.readByte(position++));
      }
    }
  }

  private static class BytesRefIterator {
    private final byte[][] data;
    private final int size;
    private int pos = 0;
    private final BytesRefBuilder refBuilder = new BytesRefBuilder();

    BytesRefIterator(Random random, int size, int maxLength) {
      this.size = size;
      data = new byte[size][];
      for (int i = 0; i < size; i++) {
        final byte[] bytes = new byte[TestUtil.nextInt(random, 1, maxLength)];
        for (int j = 0; j < bytes.length; j++) {
          bytes[j] = (byte) random.nextInt(256);
        }
        data[i] = bytes;
      }
    }

    byte[][] getData() {
      return data;
    }

    BytesRef next() {
      if (pos < size) {
        refBuilder.copyBytes(data[pos++]);
        return refBuilder.get();
      }
      return null;
    }
  }
}