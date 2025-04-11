package com.google.common.io;

import org.junit.Test;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import static org.mockito.Mockito.*;

public class CloseablesTest {

    @Test
    public void testClose_NullCloseable() throws IOException {
        Closeables.close(null, true);
        Closeables.close(null, false);
    }

    @Test
    public void testClose_CloseableWithoutIOException() throws IOException {
        Closeable mockCloseable = mock(Closeable.class);
        Closeables.close(mockCloseable, true);
        verify(mockCloseable).close();
    }

    @Test
    public void testClose_CloseableWithIOException_Swallow() throws IOException {
        Closeable mockCloseable = mock(Closeable.class);
        doThrow(new IOException("Test exception")).when(mockCloseable).close();
        Closeables.close(mockCloseable, true);
        verify(mockCloseable).close();
    }

    @Test(expected = IOException.class)
    public void testClose_CloseableWithIOException_NotSwallow() throws IOException {
        Closeable mockCloseable = mock(Closeable.class);
        doThrow(new IOException("Test exception")).when(mockCloseable).close();
        Closeables.close(mockCloseable, false);
    }

    @Test
    public void testCloseQuietly_InputStream_Null() {
        Closeables.closeQuietly((InputStream) null);
    }

    @Test
    public void testCloseQuietly_InputStream() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        Closeables.closeQuietly(mockInputStream);
        verify(mockInputStream).close();
    }

    @Test
    public void testCloseQuietly_InputStreamWithIOException() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        doThrow(new IOException("Test exception")).when(mockInputStream).close();
        Closeables.closeQuietly(mockInputStream);
        verify(mockInputStream).close();
    }

    @Test
    public void testCloseQuietly_Reader_Null() {
        Closeables.closeQuietly((Reader) null);
    }

    @Test
    public void testCloseQuietly_Reader() throws IOException {
        Reader mockReader = mock(Reader.class);
        Closeables.closeQuietly(mockReader);
        verify(mockReader).close();
    }

    @Test
    public void testCloseQuietly_ReaderWithIOException() throws IOException {
        Reader mockReader = mock(Reader.class);
        doThrow(new IOException("Test exception")).when(mockReader).close();
        Closeables.closeQuietly(mockReader);
        verify(mockReader).close();
    }
}