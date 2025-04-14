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

public class TestCharsRef extends LuceneTestCase {

  public void testUTF16SortedAsUTF8SortOrder() {
    String[] utf8Strings =
        new String[] {
          "\uD834\uDD1E",
          "\uFFFF",
          "\uD852\uDF62\uD843\uDFFC",
          "\u0020\uD842\uDC68",
          "\u0041\uD801\uDC00",
          "\u0041\uD801\uDC01",
          "a",
          "b",
          "c",
          "\uD84C\uDFB4",
          "\uD84C\uDFB4\uD844\uDE3D\uD842\uDFB7"
        };

    CharsRef[] utf16SortedAsUTF8 = new CharsRef[utf8Strings.length];
    for (int i = 0; i < utf8Strings.length; i++) {
      utf16SortedAsUTF8[i] = new CharsRef(utf8Strings[i]);
    }
    Arrays.sort(utf16SortedAsUTF8, CharsRef.getUTF16SortedAsUTF8Comparator());

    for (int i = 0; i < utf16SortedAsUTF8.length - 1; i++) {
      String a = utf16SortedAsUTF8[i].toString();
      String b = utf16SortedAsUTF8[i + 1].toString();
      byte[] aBytes = a.getBytes(java.nio.charset.StandardCharsets.UTF_8);
      byte[] bBytes = b.getBytes(java.nio.charset.StandardCharsets.UTF_8);
      assertTrue(Arrays.compareUnsigned(aBytes, bBytes) < 0);
    }
  }
}