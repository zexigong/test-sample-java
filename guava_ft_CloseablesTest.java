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
import static org.junit.Assert.fail;

import com.google.common.annotations.GwtIncompatible;
import java.io.Closeable;
import java.io.IOException;
import org.junit.Test;

/**
 * Unit test for {@link Closeables}.
 *
 * @author Michael Lancaster
 */
@GwtIncompatible // java.io.Closeable
public class CloseablesTest {
  private static class TestCloseable implements Closeable {
    boolean closed = false;
    boolean throwExceptionOnClose = false;

    @Override
    public void close() throws IOException {
      closed = true;
      if (throwExceptionOnClose) {
        throw new IOException();
      }
    }
  }

  @Test
  public void testClose() throws IOException {
    TestCloseable closeable = new TestCloseable();
    assertThat(closeable.closed).isFalse();

    Closeables.close(closeable, false);
    assertThat(closeable.closed).isTrue();
  }

  @Test
  public void testNullCloseable() throws IOException {
    Closeables.close(null, false);
  }

  @Test
  public void testIOExceptionThrown() {
    TestCloseable closeable = new TestCloseable();
    closeable.throwExceptionOnClose = true;
    try {
      Closeables.close(closeable, false);
      fail("Expected IOException");
    } catch (IOException expected) {
    }
  }

  @Test
  public void testSwallowIOException() throws IOException {
    TestCloseable closeable = new TestCloseable();
    closeable.throwExceptionOnClose = true;
    Closeables.close(closeable, true);
  }
}