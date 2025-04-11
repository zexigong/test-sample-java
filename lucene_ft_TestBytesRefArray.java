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

import java.util.Arrays;
import java.util.Comparator;
import org.apache.lucene.util.BytesRefArray.IndexedBytesRefIterator;
import org.apache.lucene.util.BytesRefArray.SortState;

public class TestBytesRefArray extends LuceneTestCase {

  public void testAppend() {
    BytesRefArray array = new BytesRefArray(Counter.newCounter());
    byte[] as = new byte[500];
    for (int i = 0; i < as.length; i++) {
      as[i] = (byte) i;
    }
    int numIters = atLeast(10);
    for (int j = 0; j < numIters; j++) {
      int num = atLeast(100);
      BytesRefBuilder ref = new BytesRefBuilder();
      for (int i = 0; i < num; i++) {
        ref.append(as, random().nextInt(as.length), random().nextInt(10));
        ref.append(as, random().nextInt(as.length), random().nextInt(10));
        array.append(ref.get());
      }
    }
    array.clear();
  }

  public void testSort() throws Exception {
    int numValues = atLeast(1000);
    BytesRefBuilder ref = new BytesRefBuilder();
    BytesRefArray array = new BytesRefArray(Counter.newCounter());
    for (int i = 0; i < numValues; i++) {
      ref.clear();
      ref.append(TestUtil.randomRealisticUnicodeString(random(), 1000));
      array.append(ref.get());
    }

    SortState sorted = array.sort(Comparator.naturalOrder(), false);
    IndexedBytesRefIterator iter = array.iterator(sorted);
    BytesRef last = null;
    while ((ref = iter.next()) != null) {
      if (last == null) {
        last = BytesRef.deepCopyOf(ref.get());
        continue;
      }
      assertTrue("values are unsorted", last.compareTo(ref.get()) <= 0);
    }
  }

  public void testSortTwoValues() throws Exception {
    BytesRefBuilder ref = new BytesRefBuilder();
    BytesRefArray array = new BytesRefArray(Counter.newCounter());
    ref.append("X");
    array.append(ref.get());
    ref.clear();
    ref.append("A");
    array.append(ref.get());

    SortState sorted = array.sort(Comparator.naturalOrder(), false);
    IndexedBytesRefIterator iter = array.iterator(sorted);
    BytesRef last = null;
    while ((ref = iter.next()) != null) {
      if (last == null) {
        last = BytesRef.deepCopyOf(ref.get());
        continue;
      }
      assertTrue("values are unsorted", last.compareTo(ref.get()) <= 0);
    }
  }

  public void testSortEmptyValues() throws Exception {
    BytesRefArray array = new BytesRefArray(Counter.newCounter());
    SortState sorted = array.sort(Comparator.naturalOrder(), false);
    IndexedBytesRefIterator iter = array.iterator(sorted);
    assertNull(iter.next());
  }

  public void testIteratorWithEmptyLastSlot() throws Exception {
    BytesRefBuilder ref = new BytesRefBuilder();
    BytesRefArray array = new BytesRefArray(Counter.newCounter());
    ref.append("test");
    array.append(ref.get());
    array.append(BytesRef.EMPTY_BYTES);
    SortState sorted = array.sort(Comparator.naturalOrder(), false);
    IndexedBytesRefIterator iter = array.iterator(sorted);
    BytesRef r;
    while ((r = iter.next()) != null) {
      assertEquals(0, r.length);
    }
  }

  public void testRandom() throws Exception {
    int numValues = atLeast(1000);
    String[] values = new String[numValues];
    for (int i = 0; i < numValues; i++) {
      values[i] = TestUtil.randomRealisticUnicodeString(random(), 1000);
    }
    Arrays.sort(values);
    BytesRefBuilder ref = new BytesRefBuilder();
    BytesRefArray array = new BytesRefArray(Counter.newCounter());
    for (String string : values) {
      ref.clear();
      ref.append(string);
      array.append(ref.get());
    }
    SortState sorted = array.sort(Comparator.naturalOrder(), false);
    IndexedBytesRefIterator iter = array.iterator(sorted);
    int i = 0;
    while ((ref = iter.next()) != null) {
      assertEquals(values[i++], ref.get().utf8ToString());
    }
  }
}