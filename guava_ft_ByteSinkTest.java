/*
 * Copyright (C) 2012 The Guava Authors
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

import static com.google.common.io.TestOption.CLOSE_THROWS;
import static com.google.common.io.TestOption.OPEN_THROWS;
import static com.google.common.io.TestOption.WRITE_THROWS;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.ImmutableSet;
import com.google.common.testing.NullPointerTester;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;

/**
 * Tests for the default methods in {@link ByteSink}.
 *
 * @author Colin Decker
 */
public class ByteSinkTest extends IoTestCase {

  private static final byte[] BYTES = newPreFilledByteArray(10000);

  private static TestByteSink sink() {
    return new TestByteSink();
  }

  private static TestByteSink sink(TestOption... options) {
    return new TestByteSink(options);
  }

  public void testNulls() throws Exception {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicInstanceMethods(sink());
  }

  public void testOpenBufferedStream() throws IOException {
    TestByteSink sink = sink();
    ByteStreams.exhaust(sink.openBufferedStream());
    sink.assertOpenCalled();
  }

  public void testWrite() throws IOException {
    TestByteSink sink = sink();
    sink.write(BYTES);
    sink.assertBytesWritten(BYTES);
  }

  public void testWrite_doesNotCloseIfWriterFails() throws IOException {
    final IOException writeException = new IOException();
    assertThrows(
        IOException.class,
        () ->
            new TestByteSink(WRITE_THROWS) {
              @Override
              protected void beforeClose(OutputStream out) throws IOException {
                throw writeException;
              }
            }.write(BYTES));
  }

  public void testWrite_fromInputStream() throws IOException {
    TestByteSink sink = sink();
    long count = sink.writeFrom(new ByteArrayInputStream(BYTES));
    sink.assertBytesWritten(BYTES);
    assertEquals(BYTES.length, count);
  }

  public void testWrite_fromInputStream_doesNotCloseIfWriterFails() throws IOException {
    final IOException writeException = new IOException();
    assertThrows(
        IOException.class,
        () ->
            new TestByteSink(WRITE_THROWS) {
              @Override
              protected void beforeClose(OutputStream out) throws IOException {
                throw writeException;
              }
            }.writeFrom(new ByteArrayInputStream(BYTES)));
  }

  public void testWrite_openThrows() {
    assertThrows(IOException.class, () -> sink(OPEN_THROWS).write(BYTES));
  }

  public void testWriteFrom_openThrows() {
    assertThrows(
        IOException.class, () -> sink(OPEN_THROWS).writeFrom(new ByteArrayInputStream(BYTES)));
  }

  public void testWrite_writeThrows() {
    assertThrows(IOException.class, () -> sink(WRITE_THROWS).write(BYTES));
  }

  public void testWriteFrom_writeThrows() {
    assertThrows(
        IOException.class, () -> sink(WRITE_THROWS).writeFrom(new ByteArrayInputStream(BYTES)));
  }

  public void testWrite_closeThrows() {
    assertThrows(IOException.class, () -> sink(CLOSE_THROWS).write(BYTES));
  }

  public void testWriteFrom_closeThrows() {
    assertThrows(
        IOException.class, () -> sink(CLOSE_THROWS).writeFrom(new ByteArrayInputStream(BYTES)));
  }

  public void testAsCharSink() throws IOException {
    Charset[] charsets = {StandardCharsets.UTF_8, StandardCharsets.US_ASCII};

    for (Charset charset : charsets) {
      TestByteSink sink = sink();
      CharSink charSink = sink.asCharSink(charset);

      String string = "éáűőúöüóí";
      charSink.write(string);
      assertEquals(string, new String(sink.getBytes(), charset));

      charSink.write("");
      assertEquals("", new String(sink.getBytes(), charset));

      charSink.write("hello");
      assertEquals("hello", new String(sink.getBytes(), charset));
    }
  }

  private static final class TestByteSink extends ByteSink {

    private final TestOption[] options;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private boolean openCalled;

    TestByteSink(TestOption... options) {
      this.options = options;
    }

    @Override
    public OutputStream openStream() throws IOException {
      openCalled = true;
      if (OPEN_THROWS.isIn(options)) {
        throw new IOException();
      }
      return new TestOutputStream(out, options) {
        @Override
        public void close() throws IOException {
          beforeClose(out);
          super.close();
        }
      };
    }

    protected void beforeClose(@SuppressWarnings("unused") OutputStream out) throws IOException {}

    void assertOpenCalled() {
      assertTrue(openCalled);
    }

    void assertBytesWritten(byte[] expected) {
      assertEquals(expected.length, out.size());
      assertEquals(0, TestUtils.difference(expected, out.toByteArray()));
    }

    byte[] getBytes() {
      return out.toByteArray();
    }
  }

  public static class TestOutputStream extends OutputStream {

    private final OutputStream out;
    private final Set<TestOption> options;
    private boolean closed;

    public TestOutputStream(OutputStream out, TestOption... options) {
      this.out = out;
      this.options = ImmutableSet.copyOf(options);
    }

    @Override
    public void write(int b) throws IOException {
      write(new byte[] {(byte) b});
    }

    @Override
    public void write(@Nullable byte[] bytes) throws IOException {
      if (closed) {
        throw new IOException("Stream is closed.");
      }
      if (WRITE_THROWS.isIn(options)) {
        throw new IOException();
      }
      out.write(bytes);
    }

    @Override
    public void close() throws IOException {
      if (CLOSE_THROWS.isIn(options)) {
        throw new IOException();
      }
      closed = true;
      out.close();
    }
  }
}