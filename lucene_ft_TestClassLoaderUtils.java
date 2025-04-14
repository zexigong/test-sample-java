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

import java.net.URL;
import java.net.URLClassLoader;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.junit.Test;

public class TestClassLoaderUtils extends LuceneTestCase {

  @Test
  public void testIsParent() {
    assertTrue(
        ClassLoaderUtils.isParentClassLoader(getClass().getClassLoader(), getClass().getClassLoader()));
    assertTrue(ClassLoaderUtils.isParentClassLoader(null, getClass().getClassLoader()));
    assertFalse(ClassLoaderUtils.isParentClassLoader(getClass().getClassLoader(), null));
    assertFalse(ClassLoaderUtils.isParentClassLoader(null, null));
    URLClassLoader ucl =
        new URLClassLoader(new URL[] {}, getClass().getClassLoader()) {
          @Override
          public String toString() {
            return "testIsParent";
          }
        };
    assertTrue(ClassLoaderUtils.isParentClassLoader(getClass().getClassLoader(), ucl));
    assertFalse(ClassLoaderUtils.isParentClassLoader(ucl, getClass().getClassLoader()));
    assertFalse(ClassLoaderUtils.isParentClassLoader(getClass().getClassLoader(), null));
    assertFalse(ClassLoaderUtils.isParentClassLoader(ucl, null));
    assertFalse(ClassLoaderUtils.isParentClassLoader(null, ucl));
    assertTrue(ClassLoaderUtils.isParentClassLoader(ucl, ucl));
    URLClassLoader ucl2 =
        new URLClassLoader(new URL[] {}, ucl) {
          @Override
          public String toString() {
            return "testIsParent2";
          }
        };
    assertTrue(ClassLoaderUtils.isParentClassLoader(ucl, ucl2));
    assertFalse(ClassLoaderUtils.isParentClassLoader(ucl2, ucl));
  }
}