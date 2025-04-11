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

import java.io.Flushable;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for {@link Flushables}.
 *
 * @author Michael Lancaster
 */
@RunWith(JUnit4.class)
public class FlushablesTest {

  private static class TestFlushable implements Flushable {
    private boolean throwException;
    private boolean flushed;

    @Override
    public void flush() throws IOException {
      if (throwException) {
        throw new IOException("TestFlushable");
      }
      flushed = true;
    }

    public void setThrowException(boolean throwException) {
      this.throwException = throwException;
    }

    public boolean wasFlushed() {
      return flushed;
    }
  }

  private TestFlushable flushable;

  @Before
  public void setUp() {
    flushable = new TestFlushable();
  }

  @After
  public void tearDown() {
    flushable = null;
  }

  @Test
  public void testFlush_noIOException() throws IOException {
    Flushables.flush(flushable, false);
    assertThat(flushable.wasFlushed()).isTrue();
  }

  @Test
  public void testFlush_noIOExceptionSwallow() throws IOException {
    Flushables.flush(flushable, true);
    assertThat(flushable.wasFlushed()).isTrue();
  }

  @Test
  public void testFlush_IOException() {
    flushable.setThrowException(true);
    try {
      Flushables.flush(flushable, false);
      fail("Expected IOException.");
    } catch (IOException expected) {
    }
  }

  @Test
  public void testFlush_IOExceptionSwallow() throws IOException {
    flushable.setThrowException(true);
    Flushables.flush(flushable, true);
  }

  @Test
  public void testFlushQuietly_noIOException() {
    Flushables.flushQuietly(flushable);
    assertThat(flushable.wasFlushed()).isTrue();
  }

  @Test
  public void testFlushQuietly_IOException() {
    flushable.setThrowException(true);
    Flushables.flushQuietly(flushable);
  }
}