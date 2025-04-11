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

import com.google.common.testing.TestLogHandler;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Unit test for {@link Closeables}.
 *
 * @author Ben Yu
 */
public class CloseablesTest extends TestCase {

  private static final String MSG = "I am alive!";

  private final TestLogHandler logHandler = new TestLogHandler();

  @Override
  protected void setUp() {
    Closeables.logger.addHandler(logHandler);
  }

  @Override
  protected void tearDown() {
    Closeables.logger.removeHandler(logHandler);
  }

  public void testClose_IOException() {
    final IOException e = new IOException();
    Closeable closeable =
        new Closeable() {
          @Override
          public void close() throws IOException {
            throw e;
          }
        };
    IOException thrown = assertThrows(IOException.class, () -> Closeables.close(closeable, false));
    assertThat(thrown).isSameInstanceAs(e);
  }

  public void testClose_swallowsIOException() throws IOException {
    Closeable closeable =
        new Closeable() {
          @Override
          public void close() throws IOException {
            throw new IOException(MSG);
          }
        };
    Closeables.close(closeable, true);
    assertThat(logHandler.getStoredLogRecords()).hasSize(1);
    LogRecord logRecord = logHandler.getStoredLogRecords().get(0);
    assertThat(logRecord.getLevel()).isEqualTo(Level.WARNING);
    assertThat(logRecord.getThrown()).isInstanceOf(IOException.class);
    assertThat(logRecord.getThrown()).hasMessageThat().isEqualTo(MSG);
  }

  public void testCloseQuietly_null() {
    Closeables.closeQuietly((OutputStream) null);
    // pass
  }

  public void testCloseQuietly_logsIOException() {
    OutputStream stream =
        new OutputStream() {
          @Override
          public void write(int b) {}

          @Override
          public void close() throws IOException {
            throw new IOException(MSG);
          }
        };

    Closeables.closeQuietly(stream);
    assertThat(logHandler.getStoredLogRecords()).hasSize(1);
    LogRecord logRecord = logHandler.getStoredLogRecords().get(0);
    assertThat(logRecord.getLevel()).isEqualTo(Level.WARNING);
    assertThat(logRecord.getThrown()).isInstanceOf(IOException.class);
    assertThat(logRecord.getThrown()).hasMessageThat().isEqualTo(MSG);
  }

  public void testCloseQuietly_AssertionError() {
    OutputStream stream =
        new OutputStream() {
          @Override
          public void write(int b) {}

          @Override
          public void close() throws IOException {
            throw new AssertionError(MSG);
          }
        };

    // Log handler will get notified that an exception is thrown. It's a bit weird but it's fine.
    AssertionError e = assertThrows(AssertionError.class, () -> Closeables.closeQuietly(stream));
    assertThat(e).hasMessageThat().isEqualTo(MSG);
  }

  public void testCloseQuietly_RuntimeException() {
    OutputStream stream =
        new OutputStream() {
          @Override
          public void write(int b) {}

          @Override
          public void close() {
            throw new RuntimeException(MSG);
          }
        };

    // Log handler will get notified that an exception is thrown. It's a bit weird but it's fine.
    RuntimeException e = assertThrows(RuntimeException.class, () -> Closeables.closeQuietly(stream));
    assertThat(e).hasMessageThat().isEqualTo(MSG);
  }

  public void testCloseNull() throws IOException {
    Closeables.close(null, true);
    Closeables.close(null, false);
  }

  public void testCloseQuietlyNull() {
    Closeables.closeQuietly((Closeable) null);
    Closeables.closeQuietly((OutputStream) null);
  }

  public void testCloseQuietlyCloseable_logsIOException() {
    Closeable stream =
        new Closeable() {
          @Override
          public void close() throws IOException {
            throw new IOException(MSG);
          }
        };

    Closeables.closeQuietly(stream);
    assertThat(logHandler.getStoredLogRecords()).hasSize(1);
    LogRecord logRecord = logHandler.getStoredLogRecords().get(0);
    assertThat(logRecord.getLevel()).isEqualTo(Level.WARNING);
    assertThat(logRecord.getThrown()).isInstanceOf(IOException.class);
    assertThat(logRecord.getThrown()).hasMessageThat().isEqualTo(MSG);
  }

  public void testCloseQuietlyCloseable_AssertionError() {
    Closeable stream =
        new Closeable() {
          @Override
          public void close() {
            throw new AssertionError(MSG);
          }
        };

    // Log handler will get notified that an exception is thrown. It's a bit weird but it's fine.
    AssertionError e = assertThrows(AssertionError.class, () -> Closeables.closeQuietly(stream));
    assertThat(e).hasMessageThat().isEqualTo(MSG);
  }

  public void testCloseQuietlyCloseable_RuntimeException() {
    Closeable stream =
        new Closeable() {
          @Override
          public void close() {
            throw new RuntimeException(MSG);
          }
        };

    // Log handler will get notified that an exception is thrown. It's a bit weird but it's fine.
    RuntimeException e = assertThrows(RuntimeException.class, () -> Closeables.closeQuietly(stream));
    assertThat(e).hasMessageThat().isEqualTo(MSG);
  }
}