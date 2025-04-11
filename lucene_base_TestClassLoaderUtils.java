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

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestClassLoaderUtils {

  @Test
  public void testSameClassLoader() {
    ClassLoader cl = getClass().getClassLoader();
    assertTrue(ClassLoaderUtils.isParentClassLoader(cl, cl));
  }

  @Test
  public void testParentClassLoader() {
    ClassLoader parent = getClass().getClassLoader();
    ClassLoader child = new ClassLoader(parent) {};
    assertTrue(ClassLoaderUtils.isParentClassLoader(parent, child));
  }

  @Test
  public void testNotParentClassLoader() {
    ClassLoader parent = new ClassLoader(null) {};
    ClassLoader child = getClass().getClassLoader();
    assertFalse(ClassLoaderUtils.isParentClassLoader(parent, child));
  }

  @Test
  public void testGrandParentClassLoader() {
    ClassLoader grandparent = getClass().getClassLoader();
    ClassLoader parent = new ClassLoader(grandparent) {};
    ClassLoader child = new ClassLoader(parent) {};
    assertTrue(ClassLoaderUtils.isParentClassLoader(grandparent, child));
  }

  @Test
  public void testNullParent() {
    ClassLoader child = getClass().getClassLoader();
    assertFalse(ClassLoaderUtils.isParentClassLoader(null, child));
  }

  @Test
  public void testNullChild() {
    ClassLoader parent = getClass().getClassLoader();
    assertFalse(ClassLoaderUtils.isParentClassLoader(parent, null));
  }
}