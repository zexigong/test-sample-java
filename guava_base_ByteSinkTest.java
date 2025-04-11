package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

public class ByteSinkTest {

  private ByteSink byteSink;

  @Before
  public void setUp() {
    byteSink = new ByteSink() {
      @Override
      public OutputStream openStream() {
        return new ByteArrayOutputStream();
      }
    };
  }

  @Test
  public void testOpenStream() throws IOException {
    try (OutputStream outputStream = byteSink.openStream()) {
      assertNotNull(outputStream);
    }
  }

  @Test
  public void testOpenBufferedStream() throws IOException {
    try (OutputStream outputStream = byteSink.openBufferedStream()) {
      assertNotNull(outputStream);
    }
  }

  @Test
  public void testWrite() throws IOException {
    byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
    byteSink.write(data);

    try (OutputStream outputStream = byteSink.openStream()) {
      ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
      assertEquals("test data", byteArrayOutputStream.toString(StandardCharsets.UTF_8.name()));
    }
  }

  @Test
  public void testWrite_nullBytes_throwsException() {
    assertThrows(NullPointerException.class, () -> byteSink.write(null));
  }

  @Test
  public void testWriteFrom() throws IOException {
    byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    long bytesWritten = byteSink.writeFrom(inputStream);

    assertEquals(data.length, bytesWritten);
  }

  @Test
  public void testWriteFrom_nullInputStream_throwsException() {
    assertThrows(NullPointerException.class, () -> byteSink.writeFrom(null));
  }

  @Test
  public void testAsCharSink() throws IOException {
    CharSink charSink = byteSink.asCharSink(StandardCharsets.UTF_8);
    assertNotNull(charSink);

    try (Writer writer = charSink.openStream()) {
      writer.write("test data");
    }

    try (OutputStream outputStream = byteSink.openStream()) {
      ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
      assertEquals("test data", byteArrayOutputStream.toString(StandardCharsets.UTF_8.name()));
    }
  }
}