/*
 * Copyright (C) 2009 The Guava Authors
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

import static com.google.common.base.StandardSystemProperty.LINE_SEPARATOR;
import static com.google.common.io.CharStreams.asWriter;
import static com.google.common.io.CharStreams.copy;
import static com.google.common.io.CharStreams.createBuffer;
import static com.google.common.io.CharStreams.exhaust;
import static com.google.common.io.CharStreams.nullWriter;
import static com.google.common.io.CharStreams.readLines;
import static com.google.common.io.CharStreams.skipFully;
import static com.google.common.io.CharStreams.toString;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.google.common.testing.NullPointerTester;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for {@link CharStreams}.
 *
 * @author Ben Yu
 */
@RunWith(JUnit4.class)
public class CharStreamsTest {

  @Test
  public void testToString() throws Exception {
    StringReader r = new StringReader("abc");
    assertEquals("abc", toString(r));
  }

  @Test
  public void testReadLines() throws Exception {
    Reader r = new StringReader("a\nb\nc");
    List<String> result = readLines(r);
    assertEquals(ImmutableList.of("a", "b", "c"), result);
  }

  @Test
  public void testReadLines_empty() throws IOException {
    Reader r = new StringReader("");
    List<String> result = readLines(r);
    assertEquals(ImmutableList.of(), result);
  }

  @Test
  public void testSkipFully() throws IOException {
    Reader r = new StringReader("abcdef");
    skipFully(r, 3);
    assertEquals('d', r.read());
    skipFully(r, 2);
    assertEquals('f', r.read());
  }

  @Test
  public void testSkipFully_eof() throws IOException {
    Reader r = new StringReader("abcde");
    skipFully(r, 5);
    try {
      skipFully(r, 1);
      fail();
    } catch (EOFException expected) {
    }
  }

  @Test
  public void testExhaust() throws IOException {
    Reader r = new StringReader("abcde");
    assertEquals(5, exhaust(r));
  }

  @Test
  public void testCopy() throws IOException {
    StringReader r = new StringReader("abcde");
    StringWriter w = new StringWriter();
    assertEquals(5, copy(r, w));
    assertEquals("abcde", w.toString());
  }

  @Test
  public void testAsWriter() throws IOException {
    StringBuilder builder = new StringBuilder();
    Writer writer = asWriter(builder);
    writer.write("foo");
    assertEquals("foo", builder.toString());
    writer.write("bar".toCharArray());
    assertEquals("foobar", builder.toString());
    writer.write("baz".toCharArray(), 0, 3);
    assertEquals("foobarbaz", builder.toString());
    writer.flush();
    writer.close();
    assertEquals("foobarbaz", builder.toString());
  }

  @Test
  public void testAsWriter_append() throws IOException {
    StringBuilder builder = new StringBuilder();
    Writer writer = asWriter(builder);
    writer.append("foo");
    assertEquals("foo", builder.toString());
    writer.append("bar", 0, 3);
    assertEquals("foobar", builder.toString());
    writer.append('x');
    assertEquals("foobarx", builder.toString());
    writer.flush();
    writer.close();
    assertEquals("foobarx", builder.toString());
  }

  @Test
  public void testCopyWithLargeBuffer() throws IOException {
    Reader r = newReader(LINE_SEPARATOR.value());
    Writer w = new StringWriter();
    assertEquals(LINE_SEPARATOR.value().length(), copy(r, w));
    assertEquals(LINE_SEPARATOR.value(), w.toString());
  }

  /**
   * Returns a reader that returns exactly one buffer of data. This assumes that the buffer size is
   * at least 32k, which is currently true.
   */
  private static Reader newReader(String suffix) {
    char[] data = new char[1024 * 32 - suffix.length()];
    return new BufferedReader(new StringReader(new String(data) + suffix));
  }

  @Test
  public void testCreateBuffer() {
    CharBuffer buf = createBuffer();
    assertEquals(0, buf.position());
    assertTrue(buf.hasArray());
    assertEquals(0x800, buf.capacity());
    assertEquals(0x800, buf.array().length);
  }

  @Test
  public void testNullWriter() throws IOException {
    Writer writer = nullWriter();
    writer.write('x');
    writer.write("foobar");
    writer.write("foobar", 1, 3);
    writer.write(new char[] {'b', 'a', 'r'});
    writer.write(new char[] {'b', 'a', 'r'}, 1, 2);
    writer.append("foobar");
    writer.append("foobar", 1, 3);
    writer.append('x');
    writer.flush();
    writer.close();
    assertEquals("CharStreams.nullWriter()", writer.toString());
  }

  @Test
  public void testCopyLarge() throws IOException {
    StringBuilder source = new StringBuilder();
    for (int i = 0; i < 100000; i++) {
      source.append("The quick brown fox jumped over the lazy dog.\n");
    }
    Path path = Jimfs.newFileSystem(Configuration.unix()).getPath("tmp");
    Files.write(path, source.toString().getBytes(Charsets.UTF_8));
    // ensure the file is really large
    assertTrue(Files.size(path) > (long) Integer.MAX_VALUE);

    // run the test
    StringWriter sw = new StringWriter();
    try (Reader reader =
        Files.newBufferedReader(path, Charsets.UTF_8, StandardOpenOption.READ)) {
      CharStreams.copy(reader, sw);
    }
    assertEquals(source.toString(), sw.toString());
  }

  @Test
  public void testNulls() throws Exception {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(CharStreams.class);
  }
}