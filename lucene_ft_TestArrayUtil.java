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
import org.apache.lucene.tests.util.LuceneTestCase;

public class TestArrayUtil extends LuceneTestCase {

  static class ArrayUtilComparator implements Comparator<BytesRef> {
    @Override
    public int compare(BytesRef a, BytesRef b) {
      return a.compareTo(b);
    }
  }

  public void testQuickSortBytesRef() {
    final int numBytes = random().nextInt(5) + 1;
    BytesRefArray refArray = new BytesRefArray(Counter.newCounter());
    BytesRef ref = new BytesRef();
    final int entries = random().nextInt(1000) + 1;
    for (int i = 0; i < entries; i++) {
      ref.bytes = new byte[numBytes];
      ref.offset = 0;
      ref.length = numBytes;
      random().nextBytes(ref.bytes);
      refArray.append(ref);
    }

    BytesRef[] arr = new BytesRef[entries];
    BytesRefIterator iter = refArray.iterator();
    int i = 0;
    while ((ref = iter.next()) != null) {
      arr[i++] = BytesRef.deepCopyOf(ref);
    }

    // sort with ArrayUtil
    ArrayUtil.introSort(arr, new ArrayUtilComparator());

    // sort with legacy Arrays.sort and check
    Arrays.sort(arr, new ArrayUtilComparator());
    BytesRef last = null;
    iter = refArray.iterator();
    i = 0;
    while ((ref = iter.next()) != null) {
      assertTrue(arr[i].bytes != ref.bytes); // we got a deep copy
      if (last != null) {
        assertTrue(
            ref.compareTo(last) >= 0); // check for order with legacy Arrays.sort
      }
      last = ref;
    }
  }

  public void testIntroSort() {
    // it is hard to test if it is really faster than quicksort for small arrays,
    // so we just test if it works
    int[] numbers = {5, 1, 89, 253, 4, 6, 3, 2, 5, 0, 10};
    int[] sorted = {0, 1, 2, 3, 4, 5, 5, 6, 10, 89, 253};
    ArrayUtil.introSort(numbers);
    assertArrayEquals(sorted, numbers);

    Integer[] numbers2 = {5, 1, 89, 253, 4, 6, 3, 2, 5, 0, 10};
    Integer[] sorted2 = {0, 1, 2, 3, 4, 5, 5, 6, 10, 89, 253};
    ArrayUtil.introSort(numbers2);
    assertArrayEquals(sorted2, numbers2);
  }

  public void testTimSort() {
    // it is hard to test if it is really faster than quicksort for small arrays,
    // so we just test if it works
    int[] numbers = {5, 1, 89, 253, 4, 6, 3, 2, 5, 0, 10};
    int[] sorted = {0, 1, 2, 3, 4, 5, 5, 6, 10, 89, 253};
    ArrayUtil.timSort(numbers);
    assertArrayEquals(sorted, numbers);

    Integer[] numbers2 = {5, 1, 89, 253, 4, 6, 3, 2, 5, 0, 10};
    Integer[] sorted2 = {0, 1, 2, 3, 4, 5, 5, 6, 10, 89, 253};
    ArrayUtil.timSort(numbers2);
    assertArrayEquals(sorted2, numbers2);
  }

  public void testSelect() {
    // test sorting
    {
      int[] arr = new int[] {4, 3, 2, 1};
      ArrayUtil.select(arr, 0, arr.length, arr.length - 1);
      int[] expected = new int[] {1, 2, 3, 4};
      assertArrayEquals(expected, arr);
    }

    // test partial sorting
    for (int numIters = 0; numIters < 10; ++numIters) {
      int[] arr = new int[TestUtil.nextInt(random(), 0, 100)];
      for (int i = 0; i < arr.length; ++i) {
        arr[i] = random().nextInt();
      }
      int from = random().nextInt(arr.length + 1);
      int to = from + random().nextInt(arr.length - from + 1);
      int mid = random().nextInt(to - from) + from;
      int[] expected = arr.clone();
      Arrays.sort(expected, from, to);
      ArrayUtil.select(arr, from, to, mid);
      assertEquals(expected[mid], arr[mid]);
      for (int i = from; i < mid; ++i) {
        assertTrue(arr[i] <= arr[mid]);
      }
      for (int i = mid + 1; i < to; ++i) {
        assertTrue(arr[mid] <= arr[i]);
      }
    }
  }

  public void testCopySubArrayOfSubArray() {
    // test copying of sub-arrays
    {
      int[] arr = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
      int from = 5;
      int to = 10;
      int[] copied = ArrayUtil.copyOfSubArray(arr, from, to);
      assertArrayEquals(new int[] {5, 6, 7, 8, 9}, copied);
    }
  }
}