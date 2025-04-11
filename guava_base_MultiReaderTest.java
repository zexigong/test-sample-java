package com.google.common.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import org.junit.Test;

public class MultiReaderTest {

  @Test
  public void testReadSingleChar() throws IOException {
    StringReader reader1 = new StringReader("abc");
    StringReader reader2 = new StringReader("de");
    MultiReader multiReader = new MultiReader(ImmutableList.of(reader1, reader2).iterator());

    assertEquals('a', multiReader.read());
    assertEquals('b', multiReader.read());
    assertEquals('c', multiReader.read());
    assertEquals('d', multiReader.read());
    assertEquals('e', multiReader.read());
    assertEquals(-1, multiReader.read());
  }

  @Test
  public void testReadCharArray() throws IOException {
    StringReader reader1 = new StringReader("abc");
    StringReader reader2 = new StringReader("de");
    MultiReader multiReader = new MultiReader(ImmutableList.of(reader1, reader2).iterator());

    char[] buffer = new char[5];
    int bytesRead = multiReader.read(buffer, 0, buffer.length);
    assertEquals(5, bytesRead);
    assertArrayEquals(new char[] {'a', 'b', 'c', 'd', 'e'}, buffer);

    bytesRead = multiReader.read(buffer, 0, buffer.length);
    assertEquals(-1, bytesRead);
  }

  @Test
  public void testSkip() throws IOException {
    StringReader reader1 = new StringReader("abc");
    StringReader reader2 = new StringReader("de");
    MultiReader multiReader = new MultiReader(ImmutableList.of(reader1, reader2).iterator());

    assertEquals(3, multiReader.skip(3));
    assertEquals('d', multiReader.read());
    assertEquals('e', multiReader.read());
    assertEquals(0, multiReader.skip(1));
  }

  @Test
  public void testReady() throws IOException {
    StringReader reader1 = new StringReader("abc");
    StringReader reader2 = new StringReader("de");
    MultiReader multiReader = new MultiReader(ImmutableList.of(reader1, reader2).iterator());

    assertTrue(multiReader.ready());
    multiReader.read(new char[3], 0, 3);
    assertTrue(multiReader.ready());
    multiReader.read(new char[2], 0, 2);
    assertFalse(multiReader.ready());
  }

  @Test
  public void testClose() throws IOException {
    StringReader reader1 = new StringReader("abc");
    StringReader reader2 = new StringReader("de");
    MultiReader multiReader = new MultiReader(ImmutableList.of(reader1, reader2).iterator());

    multiReader.read();
    multiReader.close();
    assertEquals(-1, multiReader.read());
  }

  @Test
  public void testConstructorThrowsIOException() {
    Iterator<CharSource> iterator = ImmutableList.<CharSource>of(
      new CharSource() {
        @Override
        public Reader openStream() throws IOException {
          throw new IOException("Failed to open stream");
        }
      }
    ).iterator();

    assertThrows(IOException.class, () -> new MultiReader(iterator));
  }
}