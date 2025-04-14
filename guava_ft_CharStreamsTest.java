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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

/**
 * Unit test for {@link CharStreams}.
 *
 * @author Chris Nokleberg
 */
public class CharStreamsTest {
  private static final String LINES = "hello\nworld\n";
  private static final String EXPECTED = "HELLO\nWORLD\n";

  @Test
  public void testToString() throws IOException {
    Reader reader = newUpperCaseReader(LINES);
    assertThat(CharStreams.toString(reader)).isEqualTo(EXPECTED);
  }

  @Test
  public void testCopy() throws IOException {
    Reader reader = newUpperCaseReader(LINES);
    Writer writer = new StringWriter();
    CharStreams.copy(reader, writer);
    assertThat(writer.toString()).isEqualTo(EXPECTED);
  }

  @Test
  public void testNullWriter() throws IOException {
    Reader reader = newUpperCaseReader(LINES);
    Writer writer = CharStreams.nullWriter();
    CharStreams.copy(reader, writer);
    assertThat(writer.toString()).isEmpty();
  }

  @Test
  public void testAsWriter() throws IOException {
    StringWriter stringWriter = new StringWriter();
    Writer writer = CharStreams.asWriter(stringWriter);
    assertThat(writer).isSameInstanceAs(stringWriter);

    StringBuilder builder = new StringBuilder();
    writer = CharStreams.asWriter(builder);
    writer.write('x');
    writer.write("y".toCharArray());
    writer.write("z");
    assertThat(builder.toString()).isEqualTo("xyz");
  }

  @Test
  public void testReadLines() throws IOException {
    Reader reader = newUpperCaseReader(LINES);
    List<String> expected = Lists.newArrayList();
    expected.add("HELLO");
    expected.add("WORLD");
    assertThat(CharStreams.readLines(reader)).isEqualTo(expected);
  }

  @Test
  public void testReadLines_withLineProcessor() throws IOException {
    Reader reader = newUpperCaseReader(LINES);
    List<String> expected = Lists.newArrayList();
    expected.add("HELLO");
    expected.add("WORLD");
    List<String> result = CharStreams.readLines(reader, new LineProcessor<List<String>>() {
      final List<String> list = Lists.newArrayList();
      @Override
      public boolean processLine(String line) {
        list.add(line);
        return true;
      }

      @Override
      public List<String> getResult() {
        return list;
      }
    });
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void testExhaust() throws IOException {
    Reader reader = newUpperCaseReader(LINES);
    assertThat(CharStreams.exhaust(reader)).isEqualTo(LINES.length());
  }

  @Test
  public void testSkipFully() throws IOException {
    StringReader reader = new StringReader("abcdefg");
    CharStreams.skipFully(reader, 3);
    assertThat((char) reader.read()).isEqualTo('d');
    CharStreams.skipFully(reader, 2);
    assertThat((char) reader.read()).isEqualTo('g');
    assertThat(reader.read()).isEqualTo(-1);
  }

  @Test
  public void testSkipFullyEOF() {
    StringReader reader = new StringReader("abc");
    assertThrows(EOFException.class, new ThrowingRunnable() {
      @Override
      public void run() throws IOException {
        CharStreams.skipFully(reader, 5);
      }
    });
  }

  @Test
  public void testSkipFullyNegative() {
    StringReader reader = new StringReader("abc");
    assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
      @Override
      public void run() throws IOException {
        CharStreams.skipFully(reader, -1);
      }
    });
  }

  private static Reader newUpperCaseReader(String s) {
    return new UpperCaseReader(new StringReader(s));
  }

  private static class UpperCaseReader extends ForwardingReader {
    UpperCaseReader(Reader delegate) {
      super(delegate);
    }

    @Override
    public int read() throws IOException {
      int c = super.read();
      return (c == -1) ? c : Character.toUpperCase((char) c);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
      int numChars = super.read(cbuf, off, len);
      for (int i = off; i < off + numChars; i++) {
        cbuf[i] = Character.toUpperCase(cbuf[i]);
      }
      return numChars;
    }
  }

  @Test
  public void testReadLines_empty() throws IOException {
    StringReader reader = new StringReader("");
    assertThat(CharStreams.readLines(reader)).isEqualTo(ImmutableList.of());
  }
}