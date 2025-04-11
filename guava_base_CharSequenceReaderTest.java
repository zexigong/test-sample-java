package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.CharBuffer;
import org.junit.Before;
import org.junit.Test;

public class CharSequenceReaderTest {

  private CharSequenceReader reader;
  private final CharSequence sequence = "Hello, World!";

  @Before
  public void setUp() {
    reader = new CharSequenceReader(sequence);
  }

  @Test
  public void testReadSingleCharacter() throws IOException {
    assertEquals('H', reader.read());
    assertEquals('e', reader.read());
    assertEquals('l', reader.read());
  }

  @Test
  public void testReadCharBuffer() throws IOException {
    CharBuffer buffer = CharBuffer.allocate(5);
    int charsRead = reader.read(buffer);
    assertEquals(5, charsRead);
    buffer.flip();
    assertEquals("Hello", buffer.toString());
  }

  @Test
  public void testReadCharArray() throws IOException {
    char[] buf = new char[5];
    int charsRead = reader.read(buf, 0, buf.length);
    assertEquals(5, charsRead);
    assertEquals("Hello", new String(buf));
  }

  @Test
  public void testSkipCharacters() throws IOException {
    long skipped = reader.skip(7);
    assertEquals(7, skipped);
    assertEquals('W', reader.read());
  }

  @Test
  public void testReady() throws IOException {
    assertTrue(reader.ready());
  }

  @Test
  public void testMarkAndReset() throws IOException {
    reader.mark(10);
    reader.skip(7);
    reader.reset();
    assertEquals('H', reader.read());
  }

  @Test
  public void testClose() throws IOException {
    reader.close();
    assertThrows(IOException.class, () -> reader.read());
  }

  @Test
  public void testReadAfterEnd() throws IOException {
    reader.skip(sequence.length());
    assertEquals(-1, reader.read());
  }

  @Test
  public void testMarkSupported() {
    assertTrue(reader.markSupported());
  }

  @Test
  public void testInvalidSkip() {
    assertThrows(IllegalArgumentException.class, () -> reader.skip(-1));
  }

  @Test
  public void testInvalidMark() {
    assertThrows(IllegalArgumentException.class, () -> reader.mark(-1));
  }

  @Test
  public void testInvalidReadCharArray() {
    char[] buf = new char[5];
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, -1, 6));
  }

  @Test
  public void testInvalidReadCharBuffer() {
    assertThrows(NullPointerException.class, () -> reader.read(null));
  }
}