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

import com.google.common.testing.TestLogHandler;
import java.io.Flushable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit test for {@link Flushables}. */
@RunWith(JUnit4.class)
@AndroidIncompatible // Android's logging infrastructure ends up causing this test to fail.
public class FlushablesTest {
  private static final String LOG_MESSAGE = "IOException thrown while flushing Flushable.";

  private final TestLogHandler logHandler = new TestLogHandler();
  private final Logger logger = Logger.getLogger(Flushables.class.getName());

  @Before
  public void setUp() {
    logger.addHandler(logHandler);
  }

  @After
  public void tearDown() {
    logger.removeHandler(logHandler);
  }

  private static final class TestFlushable implements Flushable {
    boolean flushed = false;
    boolean throwIOException;
    boolean throwRuntimeException;

    @Override
    public void flush() throws IOException {
      if (throwIOException) {
        throw new IOException();
      }
      if (throwRuntimeException) {
        throw new RuntimeException();
      }
      flushed = true;
    }
  }

  @Test
  public void testFlushWithoutIOException() throws IOException {
    TestFlushable flushable = new TestFlushable();
    Flushables.flush(flushable, true);
    assertThat(flushable.flushed).isTrue();
    assertThat(logHandler.getStoredLogRecords()).isEmpty();
  }

  @Test
  public void testFlushWithIOException() throws IOException {
    TestFlushable flushable = new TestFlushable();
    flushable.throwIOException = true;
    Flushables.flush(flushable, true);
    assertThat(logHandler.getStoredLogRecords()).hasSize(1);
    LogRecord logRecord = logHandler.getStoredLogRecords().get(0);
    assertThat(logRecord.getLevel()).isEqualTo(Level.WARNING);
    assertThat(logRecord.getMessage()).isEqualTo(LOG_MESSAGE);
  }

  @Test
  public void testFlushWithIOException_throws() throws IOException {
    TestFlushable flushable = new TestFlushable();
    flushable.throwIOException = true;
    try {
      Flushables.flush(flushable, false);
      fail();
    } catch (IOException expected) {
    }
  }

  @Test
  public void testFlushWithRuntimeException() throws IOException {
    TestFlushable flushable = new TestFlushable();
    flushable.throwRuntimeException = true;
    assertThrows(RuntimeException.class, () -> Flushables.flush(flushable, true));
  }

  @Test
  public void testFlushQuietly() {
    TestFlushable flushable = new TestFlushable();
    flushable.throwIOException = true;
    Flushables.flushQuietly(flushable);
    assertThat(logHandler.getStoredLogRecords()).hasSize(1);
    LogRecord logRecord = logHandler.getStoredLogRecords().get(0);
    assertThat(logRecord.getLevel()).isEqualTo(Level.WARNING);
    assertThat(logRecord.getMessage()).isEqualTo(LOG_MESSAGE);
  }
}