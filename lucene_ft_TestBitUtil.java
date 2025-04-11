/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import static org.apache.lucene.util.BitUtil.VH_BE_LONG;
import static org.apache.lucene.util.BitUtil.VH_LE_LONG;
import static org.apache.lucene.util.BitUtil.VH_NATIVE_LONG;

import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.concurrent.Callable;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.apache.lucene.tests.util.LuceneTestCase.SuppressCodecs;

@SuppressCodecs("*") // we don't use codecs, codec randomness unnecessary for these tests
public class TestBitUtil extends LuceneTestCase {

  public void testNextHighestPowerOfTwo() {
    assertEquals(
        1 << 0,
        BitUtil.nextHighestPowerOfTwo(1 << 0)); // current highest power of two should return itself
    assertEquals(
        1 << 1,
        BitUtil.nextHighestPowerOfTwo(1 << 0 | 1 << 0)); // all lower bits set should round up
    assertEquals(
        1 << 1,
        BitUtil.nextHighestPowerOfTwo(1 << 1)); // current highest power of two should return itself
    assertEquals(
        1 << 2,
        BitUtil.nextHighestPowerOfTwo(1 << 1 | 1 << 0)); // all lower bits set should round up
    assertEquals(
        1 << 30,
        BitUtil.nextHighestPowerOfTwo(1 << 30)); // current highest power of two should return
    // itself
    assertEquals(
        1L << 31,
        BitUtil.nextHighestPowerOfTwo(1L << 30 | 1 << 29)); // all lower bits set should round up
    assertEquals(
        1L << 31,
        BitUtil.nextHighestPowerOfTwo(1L << 31)); // current highest power of two should return
    // itself
    assertEquals(
        1L << 62,
        BitUtil.nextHighestPowerOfTwo(1L << 62)); // current highest power of two should return
    // itself
    assertEquals(
        1L << 63,
        BitUtil.nextHighestPowerOfTwo(1L << 62 | 1L << 0)); // all lower bits set should round up
    assertEquals(
        1L << 63,
        BitUtil.nextHighestPowerOfTwo(1L << 63)); // current highest power of two should return
    // itself

    // 0 and negative numbers should return 1
    assertEquals(1, BitUtil.nextHighestPowerOfTwo(0));
    assertEquals(1, BitUtil.nextHighestPowerOfTwo(-1));
    assertEquals(1, BitUtil.nextHighestPowerOfTwo(-2));
    assertEquals(1, BitUtil.nextHighestPowerOfTwo(-3));
    assertEquals(1, BitUtil.nextHighestPowerOfTwo(Integer.MIN_VALUE));
    assertEquals(1, BitUtil.nextHighestPowerOfTwo(Long.MIN_VALUE));
  }

  public void testZigZagEncode() {
    for (int i = 0; i < 1000; ++i) {
      assertEquals(i, BitUtil.zigZagDecode(BitUtil.zigZagEncode(i)));
      assertEquals(-i, BitUtil.zigZagDecode(BitUtil.zigZagEncode(-i)));
      long l = random().nextLong();
      assertEquals(l, BitUtil.zigZagDecode(BitUtil.zigZagEncode(l)));
    }
  }

  public void testVarHandles() throws Exception {
    byte[] arr = new byte[8];

    assertEquals(ByteOrder.LITTLE_ENDIAN, ByteOrder.nativeOrder());
    assertEquals(ByteOrder.LITTLE_ENDIAN, BitUtil.NATIVE_BYTE_ORDER);

    for (VarHandle vh : new VarHandle[] {VH_LE_LONG, VH_NATIVE_LONG}) {
      long v = random().nextLong();
      vh.set(arr, 0, v);
      assertEquals(v, vh.get(arr, 0));
    }

    expectThrows(
        UnsupportedOperationException.class,
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            VH_BE_LONG.set(arr, 0, 0L);
            return null;
          }
        });
  }
}