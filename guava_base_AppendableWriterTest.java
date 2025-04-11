package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;

public class AppendableWriterTest {

  private StringWriter stringWriter;
  private AppendableWriter appendableWriter;

  @Before
  public void setUp() {
    stringWriter = new StringWriter();
    appendableWriter = new AppendableWriter(stringWriter);
  }

  @Test
  public void testWriteCharArray() throws IOException {
    char[] chars = {'H', 'e', 'l', 'l', 'o'};
    appendableWriter.write(chars, 0, chars.length);
    assertEquals("Hello", stringWriter.toString());
  }

  @Test
  public void testWriteInt() throws IOException {
    appendableWriter.write((int) 'A');
    assertEquals("A", stringWriter.toString());
  }

  @Test
  public void testWriteString() throws IOException {
    String str = "Hello World";
    appendableWriter.write(str);
    assertEquals(str, stringWriter.toString());
  }

  @Test
  public void testWriteStringWithOffset() throws IOException {
    String str = "Hello World";
    appendableWriter.write(str, 6, 5);
    assertEquals("World", stringWriter.toString());
  }

  @Test
  public void testAppendChar() throws IOException {
    appendableWriter.append('A');
    assertEquals("A", stringWriter.toString());
  }

  @Test
  public void testAppendCharSequence() throws IOException {
    CharSequence charSeq = "Hello";
    appendableWriter.append(charSeq);
    assertEquals("Hello", stringWriter.toString());
  }

  @Test
  public void testAppendCharSequenceWithRange() throws IOException {
    CharSequence charSeq = "Hello World";
    appendableWriter.append(charSeq, 6, 11);
    assertEquals("World", stringWriter.toString());
  }

  @Test
  public void testFlush() throws IOException {
    appendableWriter.write("Test");
    appendableWriter.flush();
    assertEquals("Test", stringWriter.toString());
  }

  @Test
  public void testClose() throws IOException {
    appendableWriter.close();
    assertThrows(IOException.class, () -> appendableWriter.write("Should fail"));
  }
}