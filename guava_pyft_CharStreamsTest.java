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

import static com.google.common.io.CharStreams.asWriter;
import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import junit.framework.TestCase;

/**
 * Unit test for {@link CharStreams}.
 *
 * @author Chris Nokleberg
 */
public class CharStreamsTest extends TestCase {
  private static final String TEXT = "This is some text\n"
      + "that should be read line-by-line\n"
      + "using the readLines method.\n";

  public void testToString() throws IOException {
    String expect = "the quick brown fox jumped over the lazy dog";
    Reader r = new StringReader(expect);
    String result = CharStreams.toString(r);
    assertEquals(expect, result);
  }

  public void testToString_empty() throws IOException {
    Reader r = new StringReader("");
    assertEquals("", CharStreams.toString(r));
  }

  public void testReadLines() throws IOException {
    Reader r = new StringReader(TEXT);
    List<String> result = CharStreams.readLines(r);
    List<String> expected = asList(
        "This is some text",
        "that should be read line-by-line",
        "using the readLines method.");
    assertEquals(expected, result);
  }

  public void testReadLines_empty() throws IOException {
    Reader r = new StringReader("");
    List<String> result = CharStreams.readLines(r);
    assertTrue(result.isEmpty());
  }

  public void testReadLines_withLineProcessor() throws IOException {
    Reader r = new StringReader(TEXT);
    List<String> result =
        CharStreams.readLines(
            r,
            new LineProcessor<List<String>>() {
              final ImmutableList.Builder<String> builder = ImmutableList.builder();

              @Override
              public boolean processLine(String line) {
                builder.add(line);
                return true;
              }

              @Override
              public List<String> getResult() {
                return builder.build();
              }
            });
    List<String> expected =
        ImmutableList.of(
            "This is some text",
            "that should be read line-by-line",
            "using the readLines method.");
    assertThat(result).isEqualTo(expected);
  }

  public void testReadLines_withLineProcessor_stopsOnFalse() throws IOException {
    Reader r = new StringReader(TEXT);
    List<String> result =
        CharStreams.readLines(
            r,
            new LineProcessor<List<String>>() {
              final ImmutableList.Builder<String> builder = ImmutableList.builder();

              @Override
              public boolean processLine(String line) {
                builder.add(line);
                return false;
              }

              @Override
              public List<String> getResult() {
                return builder.build();
              }
            });
    List<String> expected = ImmutableList.of("This is some text");
    assertThat(result).isEqualTo(expected);
  }

  public void testExhaust() throws IOException {
    String input = "this is some text";
    Reader reader = new StringReader(input);
    assertEquals(input.length(), CharStreams.exhaust(reader));
  }

  public void testSkipFully() throws IOException {
    Reader reader = new StringReader("abcdef");
    CharStreams.skipFully(reader, 2);
    assertEquals('c', reader.read());
    CharStreams.skipFully(reader, 3);
    assertEquals('f', reader.read());
    CharStreams.skipFully(reader, 1);
    assertEquals(-1, reader.read());
  }

  public void testSkipFully_eof() throws IOException {
    Reader reader = new StringReader("abcde");
    CharStreams.skipFully(reader, 5);
    assertEquals(-1, reader.read());
    assertThrows(EOFException.class, () -> CharStreams.skipFully(reader, 1));
  }

  public void testNullWriter() throws IOException {
    Writer writer = CharStreams.nullWriter();
    writer.write('a');
    writer.write("foobar");
    writer.write("foobar", 1, 3);
    writer.write(new char[] {'a', 'b', 'c'});
    writer.write(new char[] {'a', 'b', 'c'}, 1, 2);
    writer.append('x');
    writer.append("foobar");
    writer.append("foobar", 1, 3);
    writer.flush();
    writer.close();
  }

  public void testAsWriter() throws IOException {
    StringBuilder builder = new StringBuilder();
    Writer writer = asWriter(builder);
    writer.write('x');
    writer.write(new char[] {'y', 'z'});
    writer.write("abcdefg");
    writer.write("abcdefg", 1, 3);
    writer.append('X');
    writer.append("foobar");
    writer.append("foobar", 1, 3);
    writer.flush();
    writer.close();
    assertEquals("xyzabcdefbXfoobaroo", builder.toString());
  }

  public void testAsWriter_writer() throws IOException {
    StringWriter stringWriter = new StringWriter();
    Writer writer = asWriter(stringWriter);
    writer.write('x');
    writer.write(new char[] {'y', 'z'});
    writer.write("abcdefg");
    writer.write("abcdefg", 1, 3);
    writer.append('X');
    writer.append("foobar");
    writer.append("foobar", 1, 3);
    writer.flush();
    writer.close();
    assertEquals("xyzabcdefbXfoobaroo", stringWriter.toString());
  }

  public void testCopy() throws IOException {
    StringBuilder builder = new StringBuilder();
    Writer writer = asWriter(builder);
    String expect = "the quick brown fox jumped over the lazy dog";
    Reader reader = new StringReader(expect);
    long count = CharStreams.copy(reader, writer);
    assertEquals(expect.length(), count);
    assertEquals(expect, builder.toString());
  }

  public void testCopy_reader_writer() throws IOException {
    StringWriter writer = new StringWriter();
    String expect = "the quick brown fox jumped over the lazy dog";
    Reader reader = new StringReader(expect);
    long count = CharStreams.copy(reader, writer);
    assertEquals(expect.length(), count);
    assertEquals(expect, writer.toString());
  }

  public void testCopy_reader_builder() throws IOException {
    StringBuilder builder = new StringBuilder();
    String expect = "the quick brown fox jumped over the lazy dog";
    Reader reader = new StringReader(expect);
    long count = CharStreams.copy(reader, builder);
    assertEquals(expect.length(), count);
    assertEquals(expect, builder.toString());
  }

  public void testCopy_noInput() throws IOException {
    StringBuilder builder = new StringBuilder();
    Writer writer = asWriter(builder);
    String expect = "";
    Reader reader = new StringReader(expect);
    long count = CharStreams.copy(reader, writer);
    assertEquals(expect.length(), count);
    assertEquals(expect, builder.toString());
  }

  public void testNulls() {
    new NullPointerTester().testAllPublicStaticMethods(CharStreams.class);
  }
}