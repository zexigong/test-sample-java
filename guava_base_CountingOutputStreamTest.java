package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class CountingOutputStreamTest {

    private ByteArrayOutputStream byteArrayOutputStream;
    private CountingOutputStream countingOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        countingOutputStream = new CountingOutputStream(byteArrayOutputStream);
    }

    @Test
    public void testWriteSingleByte() throws IOException {
        countingOutputStream.write(1);
        assertEquals(1, countingOutputStream.getCount());
        countingOutputStream.write(2);
        assertEquals(2, countingOutputStream.getCount());
        assertEquals(2, byteArrayOutputStream.size());
    }

    @Test
    public void testWriteByteArray() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        countingOutputStream.write(data);
        assertEquals(data.length, countingOutputStream.getCount());
        assertEquals(data.length, byteArrayOutputStream.size());
    }

    @Test
    public void testWriteByteArrayWithOffsetAndLength() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        countingOutputStream.write(data, 1, 3);
        assertEquals(3, countingOutputStream.getCount());
        assertEquals(3, byteArrayOutputStream.size());
    }

    @Test
    public void testWriteByteArrayThrowsIOException() {
        OutputStream faultyOutputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Write failed");
            }
        };

        CountingOutputStream faultyCountingOutputStream = new CountingOutputStream(faultyOutputStream);
        byte[] data = {1, 2, 3};

        assertThrows(IOException.class, () -> faultyCountingOutputStream.write(data));
        assertEquals(0, faultyCountingOutputStream.getCount());
    }

    @Test
    public void testClose() throws IOException {
        countingOutputStream.write(1);
        countingOutputStream.close();
        assertEquals(1, countingOutputStream.getCount());
    }
}