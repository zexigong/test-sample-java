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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.io.Flushable;
import java.io.IOException;
import junit.framework.TestCase;

/**
 * Unit test for {@link Flushables}.
 *
 * <p>This test can't be a {@link com.google.common.testing.NullPointerTester} because {@link
 * Flushable} isn't implemented.
 *
 * @author Kevin Bourrillion
 */
public class FlushablesTest extends TestCase {
  private static class TestFlushable implements Flushable {
    boolean flushed;
    boolean fail;

    @Override
    public void flush() throws IOException {
      if (fail) {
        throw new IOException("flush failed");
      }
      flushed = true;
    }
  }

  private final TestFlushable flushable = new TestFlushable();

  public void testFlush() throws IOException {
    assertThat(flushable.flushed).isFalse();
    Flushables.flush(flushable, false);
    assertThat(flushable.flushed).isTrue();
  }

  public void testFlush_withFlushIOExceptionAndSwallowIOExceptionTrue() throws IOException {
    flushable.fail = true;
    Flushables.flush(flushable, true);
    assertThat(flushable.flushed).isFalse();
  }

  public void testFlush_withFlushIOExceptionAndSwallowIOExceptionFalse() throws IOException {
    flushable.fail = true;
    assertThrows(IOException.class, () -> Flushables.flush(flushable, false));
  }

  public void testFlushQuietly() {
    assertThat(flushable.flushed).isFalse();
    Flushables.flushQuietly(flushable);
    assertThat(flushable.flushed).isTrue();
  }

  public void testFlushQuietly_withFlushIOException() {
    flushable.fail = true;
    Flushables.flushQuietly(flushable);
    assertThat(flushable.flushed).isFalse();
  }
}