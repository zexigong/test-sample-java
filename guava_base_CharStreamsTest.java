package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import org.junit.Test;

public class CharStreamsTest {

  @Test
  public void testCopy_ReaderToWriter() throws IOException {
    String input = "Hello, World!";
    Reader reader = new StringReader(input);
    StringWriter writer = new StringWriter();

    long charsCopied = CharStreams.copy(reader, writer);

    assertEquals(input, writer.toString());
    assertEquals(input.length(), charsCopied);
  }

  @Test
  public void testCopy_ReaderToStringBuilder() throws IOException {
    String input = "Hello, World!";
    Reader reader = new StringReader(input);
    StringBuilder builder = new StringBuilder();

    long charsCopied = CharStreams.copy(reader, builder);

    assertEquals(input, builder.toString());
    assertEquals(input.length(), charsCopied);
  }

  @Test
  public void testToString() throws IOException {
    String input = "Hello, World!";
    Reader reader = new StringReader(input);

    String result = CharStreams.toString(reader);

    assertEquals(input, result);
  }

  @Test
  public void testReadLines() throws IOException {
    String input = "Line1\nLine2\nLine3";
    Reader reader = new StringReader(input);

    List<String> lines = CharStreams.readLines(reader);

    assertEquals(3, lines.size());
    assertEquals("Line1", lines.get(0));
    assertEquals("Line2", lines.get(1));
    assertEquals("Line3", lines.get(2));
  }

  @Test
  public void testExhaust() throws IOException {
    String input = "Hello, World!";
    Reader reader = new StringReader(input);

    long charsExhausted = CharStreams.exhaust(reader);

    assertEquals(input.length(), charsExhausted);
  }

  @Test
  public void testSkipFully() throws IOException {
    String input = "Hello, World!";
    Reader reader = new StringReader(input);

    CharStreams.skipFully(reader, 7);

    char[] remaining = new char[6];
    reader.read(remaining);

    assertEquals("World!", new String(remaining));
  }

  @Test
  public void testSkipFully_ThrowsEOFException() {
    String input = "Short";
    Reader reader = new StringReader(input);

    try {
      CharStreams.skipFully(reader, 10);
      fail("Expected EOFException");
    } catch (EOFException expected) {
      // expected exception
    } catch (IOException e) {
      fail("Unexpected IOException: " + e);
    }
  }

  @Test
  public void testNullWriter() throws IOException {
    Writer nullWriter = CharStreams.nullWriter();
    nullWriter.write("This text will be discarded");
    nullWriter.flush();
    nullWriter.close();
  }
}