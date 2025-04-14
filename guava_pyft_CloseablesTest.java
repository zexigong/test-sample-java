package com.google.common.io;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;

import com.google.common.testing.TestLogHandler;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 * Unit test for {@link Closeables}.
 *
 * @author Michael Lancaster
 */
public class CloseablesTest extends TestCase {
  private Closeable closeable;
  private TestLogHandler handler;
  private Logger logger;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    logger = Closeables.logger;
    handler = new TestLogHandler();
    logger.addHandler(handler);
    closeable = new Closeable() {
      @Override
      public void close() throws IOException {
        throw new IOException("kaboom");
      }
    };
  }

  @Override
  public void tearDown() throws Exception {
    closeable = null;
    logger.removeHandler(handler);
    handler = null;
    logger = null;
    super.tearDown();
  }

  public void testClose_quiet() throws IOException {
    Closeables.close(closeable, true);
    assertThat(handler.getStoredLogRecords()).hasSize(1);
    assertLogMessage(handler.getStoredLogRecords().get(0), "IOException thrown while closing Closeable.");
  }

  public void testClose_passThrough() throws IOException {
    try {
      Closeables.close(closeable, false);
      fail("Expected IOException");
    } catch (IOException expected) {
    }
  }

  public void testCloseQuietly_inputStream() throws IOException {
    Closeables.closeQuietly(new InputStream() {
      @Override
      public int read() throws IOException {
        return 0;
      }

      @Override
      public void close() throws IOException {
        throw new IOException("kaboom");
      }
    });
    assertThat(handler.getStoredLogRecords()).hasSize(1);
    assertLogMessage(handler.getStoredLogRecords().get(0), "IOException thrown while closing Closeable.");
  }

  public void testCloseQuietly_reader() throws IOException {
    Closeables.closeQuietly(new Reader() {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        return 0;
      }

      @Override
      public void close() throws IOException {
        throw new IOException("kaboom");
      }
    });
    assertThat(handler.getStoredLogRecords()).hasSize(1);
    assertLogMessage(handler.getStoredLogRecords().get(0), "IOException thrown while closing Closeable.");
  }

  public void testCloseNullAndSwallowIOException() throws IOException {
    Closeables.close(null, true);
  }

  public void testCloseNullWithoutSwallowingIOException() throws IOException {
    Closeables.close(null, false);
  }

  public void testCloseQuietlyNullInputStream() throws IOException {
    Closeables.closeQuietly((InputStream) null);
  }

  public void testCloseQuietlyNullReader() throws IOException {
    Closeables.closeQuietly((Reader) null);
  }

  public void testCloseFileDescriptor() throws Exception {
    // We can't actually close it.
    assumeFalse("This test is not supported on Windows.", System.getProperty("os.name").startsWith("Windows"));

    try {
      Closeables.close(FileDescriptor.in, false);
      fail();
    } catch (IllegalArgumentException expected) {
    }

    assertThat(handler.getStoredLogRecords()).isEmpty();
  }

  private static void assertLogMessage(LogRecord record, String expected) {
    assertThat(record.getLevel()).isEqualTo(Level.WARNING);
    assertThat(record.getThrown()).hasMessageThat().isEqualTo("kaboom");
    assertThat(record.getMessage()).isEqualTo(expected);
  }
}