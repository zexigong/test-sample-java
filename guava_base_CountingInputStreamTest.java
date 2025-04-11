package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;

public class CountingInputStreamTest {

  private CountingInputStream countingInputStream;
  private byte[] data;

  @Before
  public void setUp() {
    data = new byte[] {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    countingInputStream = new CountingInputStream(inputStream);
  }

  @Test
  public void testReadSingleByte() throws IOException {
    assertEquals(1, countingInputStream.read());
    assertEquals(1, countingInputStream.getCount());
    assertEquals(2, countingInputStream.read());
    assertEquals(2, countingInputStream.getCount());
  }

  @Test
  public void testReadIntoByteArray() throws IOException {
    byte[] buffer = new byte[3];
    int bytesRead = countingInputStream.read(buffer, 0, buffer.length);
    assertEquals(3, bytesRead);
    assertEquals(3, countingInputStream.getCount());
    assertEquals(1, buffer[0]);
    assertEquals(2, buffer[1]);
    assertEquals(3, buffer[2]);

    bytesRead = countingInputStream.read(buffer, 0, buffer.length);
    assertEquals(2, bytesRead);
    assertEquals(5, countingInputStream.getCount());
    assertEquals(4, buffer[0]);
    assertEquals(5, buffer[1]);
  }

  @Test
  public void testSkipBytes() throws IOException {
    long skipped = countingInputStream.skip(2);
    assertEquals(2, skipped);
    assertEquals(2, countingInputStream.getCount());

    assertEquals(3, countingInputStream.read());
    assertEquals(3, countingInputStream.getCount());
  }

  @Test
  public void testMarkAndReset() throws IOException {
    countingInputStream.mark(10);
    assertEquals(1, countingInputStream.read());
    assertEquals(1, countingInputStream.getCount());

    countingInputStream.reset();
    assertEquals(0, countingInputStream.getCount());
    assertEquals(1, countingInputStream.read());
  }

  @Test
  public void testResetWithoutMark() {
    InputStream inputStream = new ByteArrayInputStream(data);
    CountingInputStream cis = new CountingInputStream(inputStream);
    assertThrows(IOException.class, () -> {
      cis.reset();
    });
  }

  @Test
  public void testResetWithMarkNotSupported() {
    InputStream inputStream = new InputStream() {
      @Override
      public int read() {
        return -1; // end of stream
      }

      @Override
      public synchronized void reset() throws IOException {
        throw new IOException("Mark not supported");
      }

      @Override
      public boolean markSupported() {
        return false;
      }
    };

    CountingInputStream cis = new CountingInputStream(inputStream);
    assertThrows(IOException.class, () -> {
      cis.reset();
    });
  }
}