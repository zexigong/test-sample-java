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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

import java.io.IOException;
import org.apache.lucene.util.LuceneTestCase.Slow;
import org.junit.Test;

/** Simple tests for {@link CloseableThreadLocal} */
public class TestCloseableThreadLocal extends LuceneTestCase {

  @Test
  public void testCloseableThreadLocal() throws IOException {
    try (CloseableThreadLocal<String> ctl = new CloseableThreadLocal<>()) {
      String testString = "This is a test";
      ctl.set(testString);
      assertEquals(testString, ctl.get());
    }
  }

  @Test
  public void testLeak() throws Exception {

    class TestObject {
      private final String testString;
      private final boolean[] gcCalled;

      TestObject(boolean[] gcCalled, String testString) {
        this.gcCalled = gcCalled;
        this.testString = testString;
      }

      @Override
      protected void finalize() throws Throwable {
        gcCalled[0] = true;
        super.finalize();
      }
    }

    class TestCloseableThreadLocal extends CloseableThreadLocal<TestObject> {
      @Override
      protected TestObject initialValue() {
        return new TestObject(gcCalled, testString);
      }
    }

    String testString = "This is a test";
    boolean[] gcCalled = new boolean[] {false};

    try (CloseableThreadLocal<TestObject> ctl = new TestCloseableThreadLocal()) {
      TestObject testObject = ctl.get();
      assertEquals(testString, testObject.testString);
      assertFalse(gcCalled[0]);
    }

    // enforce gc, we need to wrap this in a retry
    // because the threadlocal might still be referenced by another thread
    // and we can't call System.gc() that often
    assertTrue(
        await(
            () -> {
              System.gc();
              return gcCalled[0];
            }));
  }

  @Test
  public void testDefaultValue() throws Exception {
    try (CloseableThreadLocal<String> ctl = new CloseableThreadLocal<>()) {
      assertNull(ctl.get());
    }
  }

  @Test
  public void testSetNull() throws Exception {
    try (CloseableThreadLocal<String> ctl = new CloseableThreadLocal<>()) {
      ctl.set("zero");
      assertEquals("zero", ctl.get());
      ctl.set(null);
      assertNull(ctl.get());
    }
  }

  // Just make sure that if two threads are accessing the same
  // CloseableThreadLocal, they do not see each other's value:
  @Slow
  public void testThreadSafety() throws Exception {
    try (CloseableThreadLocal<String> ctl = new CloseableThreadLocal<>()) {
      Thread[] threads = new Thread[2];
      final String[] threadValues = new String[2];
      for (int i = 0; i < 2; i++) {
        final int which = i;
        threads[i] =
            new Thread() {
              @Override
              public void run() {
                if (which == 0) {
                  ctl.set("zero");
                  // Sleep long enough to (hopefully) provoke
                  // thread scheduling and contention:
                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException ie) {
                    throw new RuntimeException(ie);
                  }
                } else {
                  ctl.set("one");
                }
                threadValues[which] = ctl.get();
              }
            };
        threads[i].start();
      }
      for (int i = 0; i < 2; i++) {
        threads[i].join();
      }
      assertEquals("zero", threadValues[0]);
      assertEquals("one", threadValues[1]);
    }
  }

  @Test
  public void testThrowException() {
    CloseableThreadLocal<String> ctl = new CloseableThreadLocal<>();
    ctl.close();
    IllegalStateException exception =
        expectThrows(
            IllegalStateException.class,
            () -> {
              ctl.get();
            });
    assertThat(exception.getMessage(), containsString("ThreadLocal already closed"));
    assertThat(exception.getCause(), instanceOf(NullPointerException.class));
  }
}