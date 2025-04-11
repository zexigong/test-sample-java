/*
 * Copyright (C) 2008 The Guava Authors
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

import com.google.common.testing.TestLogHandler;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link MultiReader}. */
@RunWith(JUnit4.class)
public class MultiReaderTest {
  private static final Logger logger = Logger.getLogger(MultiReader.class.getName());

  private final TestLogHandler logHandler = new TestLogHandler();

  @Before
  public void addLogHandler() {
    logger.addHandler(logHandler);
  }

  @After
  public void removeLogHandler() {
    logger.removeHandler(logHandler);
  }

  @Test
  public void testEmpty() throws IOException {
    assertThat(read(MultiReaderTest::openNothing)).isEmpty();
  }

  @Test
  public void testSingle() throws IOException {
    assertThat(read(() -> openOne("abc"))).isEqualTo("abc");
  }

  @Test
  public void testMulti() throws IOException {
    assertThat(read(() -> openMultiple("abc", "", "", "de", "fghi"))).isEqualTo("abcdefghi");
  }

  @Test
  public void testSkip() throws IOException {
    assertThat(skip(() -> openMultiple("abc", "", "", "de", "fghi"))).isEqualTo("abcdefghi");
  }

  @Test
  public void testCloseSuppressesFirstException() throws IOException {
    IOException exception = new IOException();
    IOException secondException = new IOException();
    MultiReader multiReader =
        new MultiReader(
            Arrays.asList(
                    CharSource.wrap("abc"),
                    new FailingCharSource(exception),
                    new FailingCharSource(secondException))
                .iterator());
    IOException e = assertThrows(IOException.class, multiReader::read);
    assertThat(e).isSameInstanceAs(exception);
    assertThat(logHandler.getStoredLogRecords()).hasSize(1);
    LogRecord logRecord = logHandler.getStoredLogRecords().get(0);
    assertThat(logRecord.getLevel()).isEqualTo(Level.WARNING);
    assertThat(logRecord.getThrown()).isSameInstanceAs(secondException);
  }

  @Test
  public void testCloseSuppressesLaterException() throws IOException {
    IOException exception = new IOException();
    IOException secondException = new IOException();
    MultiReader multiReader =
        new MultiReader(
            Arrays.asList(
                    CharSource.wrap("abc"),
                    new FailingCharSource(secondException),
                    new FailingCharSource(exception))
                .iterator());
    IOException e = assertThrows(IOException.class, multiReader::read);
    assertThat(e).isSameInstanceAs(secondException);
    assertThat(logHandler.getStoredLogRecords()).hasSize(1);
    LogRecord logRecord = logHandler.getStoredLogRecords().get(0);
    assertThat(logRecord.getLevel()).isEqualTo(Level.WARNING);
    assertThat(logRecord.getThrown()).isSameInstanceAs(exception);
  }

  private static class FailingCharSource extends CharSource {

    private final IOException exception;

    FailingCharSource(IOException exception) {
      this.exception = exception;
    }

    @Override
    public Reader openStream() throws IOException {
      throw exception;
    }
  }

  private static String read(CharSource source) throws IOException {
    try (Reader reader = source.openStream()) {
      char[] buf = new char[64];
      StringBuilder out = new StringBuilder();
      int r;
      while ((r = reader.read(buf)) != -1) {
        out.append(buf, 0, r);
      }
      return out.toString();
    }
  }

  private static String skip(CharSource source) throws IOException {
    try (Reader reader = source.openStream()) {
      char[] buf = new char[64];
      StringBuilder out = new StringBuilder();
      while (true) {
        long skipped = reader.skip(1);
        if (skipped == 0) {
          break;
        }
        int r = reader.read(buf);
        if (r == -1) {
          break;
        }
        out.append(buf, 0, r);
      }
      return out.toString();
    }
  }

  private static CharSource openNothing() {
    return new MultiReader(Collections.<CharSource>emptyList().iterator())::openStream;
  }

  private static CharSource openOne(final String string) {
    return CharSource.wrap(string);
  }

  private static CharSource openMultiple(final String... strings) {
    return new MultiReader(
            Arrays.stream(strings).map(CharSource::wrap).iterator())::openStream;
  }
}