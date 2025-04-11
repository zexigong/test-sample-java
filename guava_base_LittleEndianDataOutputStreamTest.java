package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class LittleEndianDataOutputStreamTest {

  private ByteArrayOutputStream byteArrayOutputStream;
  private LittleEndianDataOutputStream littleEndianDataOutputStream;

  @Before
  public void setUp() {
    byteArrayOutputStream = new ByteArrayOutputStream();
    littleEndianDataOutputStream = new LittleEndianDataOutputStream(byteArrayOutputStream);
  }

  @Test
  public void testWriteBoolean() throws IOException {
    littleEndianDataOutputStream.writeBoolean(true);
    littleEndianDataOutputStream.writeBoolean(false);

    DataOutputStream dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
    dataOutputStream.writeBoolean(true);
    dataOutputStream.writeBoolean(false);

    assertEquals(2, byteArrayOutputStream.toByteArray().length);
  }

  @Test
  public void testWriteByte() throws IOException {
    littleEndianDataOutputStream.writeByte(0xAB);

    assertEquals((byte) 0xAB, byteArrayOutputStream.toByteArray()[0]);
  }

  @Test
  public void testWriteChar() throws IOException {
    littleEndianDataOutputStream.writeChar('A');

    byte[] bytes = byteArrayOutputStream.toByteArray();
    assertEquals((byte) 'A', bytes[0]);
    assertEquals(0, bytes[1]);
  }

  @Test
  public void testWriteShort() throws IOException {
    littleEndianDataOutputStream.writeShort(0xABCD);

    byte[] bytes = byteArrayOutputStream.toByteArray();
    assertEquals((byte) 0xCD, bytes[0]);
    assertEquals((byte) 0xAB, bytes[1]);
  }

  @Test
  public void testWriteInt() throws IOException {
    littleEndianDataOutputStream.writeInt(0x12345678);

    byte[] bytes = byteArrayOutputStream.toByteArray();
    assertEquals((byte) 0x78, bytes[0]);
    assertEquals((byte) 0x56, bytes[1]);
    assertEquals((byte) 0x34, bytes[2]);
    assertEquals((byte) 0x12, bytes[3]);
  }

  @Test
  public void testWriteLong() throws IOException {
    littleEndianDataOutputStream.writeLong(0x1122334455667788L);

    byte[] bytes = byteArrayOutputStream.toByteArray();
    assertEquals((byte) 0x88, bytes[0]);
    assertEquals((byte) 0x77, bytes[1]);
    assertEquals((byte) 0x66, bytes[2]);
    assertEquals((byte) 0x55, bytes[3]);
    assertEquals((byte) 0x44, bytes[4]);
    assertEquals((byte) 0x33, bytes[5]);
    assertEquals((byte) 0x22, bytes[6]);
    assertEquals((byte) 0x11, bytes[7]);
  }

  @Test
  public void testWriteFloat() throws IOException {
    littleEndianDataOutputStream.writeFloat(1.0f);

    int intBits = Float.floatToIntBits(1.0f);
    byte[] bytes = byteArrayOutputStream.toByteArray();
    assertEquals((byte) (intBits & 0xFF), bytes[0]);
    assertEquals((byte) ((intBits >> 8) & 0xFF), bytes[1]);
    assertEquals((byte) ((intBits >> 16) & 0xFF), bytes[2]);
    assertEquals((byte) ((intBits >> 24) & 0xFF), bytes[3]);
  }

  @Test
  public void testWriteDouble() throws IOException {
    littleEndianDataOutputStream.writeDouble(1.0);

    long longBits = Double.doubleToLongBits(1.0);
    byte[] bytes = byteArrayOutputStream.toByteArray();
    assertEquals((byte) (longBits & 0xFF), bytes[0]);
    assertEquals((byte) ((longBits >> 8) & 0xFF), bytes[1]);
    assertEquals((byte) ((longBits >> 16) & 0xFF), bytes[2]);
    assertEquals((byte) ((longBits >> 24) & 0xFF), bytes[3]);
    assertEquals((byte) ((longBits >> 32) & 0xFF), bytes[4]);
    assertEquals((byte) ((longBits >> 40) & 0xFF), bytes[5]);
    assertEquals((byte) ((longBits >> 48) & 0xFF), bytes[6]);
    assertEquals((byte) ((longBits >> 56) & 0xFF), bytes[7]);
  }

  @Test
  public void testWriteBytes() throws IOException {
    littleEndianDataOutputStream.writeBytes("AB");

    byte[] bytes = byteArrayOutputStream.toByteArray();
    assertEquals((byte) 'A', bytes[0]);
    assertEquals((byte) 'B', bytes[1]);
  }

  @Test
  public void testWriteChars() throws IOException {
    littleEndianDataOutputStream.writeChars("AB");

    byte[] bytes = byteArrayOutputStream.toByteArray();
    assertEquals((byte) 'A', bytes[0]);
    assertEquals(0, bytes[1]);
    assertEquals((byte) 'B', bytes[2]);
    assertEquals(0, bytes[3]);
  }

  @Test
  public void testWriteUTF() throws IOException {
    littleEndianDataOutputStream.writeUTF("AB");

    byte[] expected = new byte[4];
    expected[0] = 0;
    expected[1] = 2; // Length
    expected[2] = (byte) 'A';
    expected[3] = (byte) 'B';

    byte[] actual = byteArrayOutputStream.toByteArray();
    assertEquals(expected.length, actual.length);
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], actual[i]);
    }
  }

  @Test
  public void testClose() throws IOException {
    littleEndianDataOutputStream.close();
    assertThrows(IOException.class, () -> littleEndianDataOutputStream.writeByte(0xAB));
  }
}