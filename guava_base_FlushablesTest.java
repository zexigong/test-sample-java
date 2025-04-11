package com.google.common.io;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.Flushable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

public class FlushablesTest {

  private Flushable mockFlushable;
  private Logger mockLogger;

  @Before
  public void setUp() {
    mockFlushable = mock(Flushable.class);
    mockLogger = mock(Logger.class);
  }

  @Test
  public void testFlush_withSwallowIOException_false_noException() {
    try {
      Flushables.flush(mockFlushable, false);
      verify(mockFlushable).flush();
    } catch (IOException e) {
      fail("IOException should not have been thrown.");
    }
  }

  @Test
  public void testFlush_withSwallowIOException_false_withException() {
    try {
      doThrow(new IOException("Test IOException")).when(mockFlushable).flush();
      Flushables.flush(mockFlushable, false);
      fail("IOException should have been thrown.");
    } catch (IOException e) {
      // expected
    }
  }

  @Test
  public void testFlush_withSwallowIOException_true_withException() {
    try {
      doThrow(new IOException("Test IOException")).when(mockFlushable).flush();
      Flushables.flush(mockFlushable, true);
      verify(mockLogger).log(eq(Level.WARNING), anyString(), any(IOException.class));
    } catch (IOException e) {
      fail("IOException should not have been thrown.");
    }
  }

  @Test
  public void testFlushQuietly_noException() {
    Flushables.flushQuietly(mockFlushable);
    try {
      verify(mockFlushable).flush();
    } catch (IOException e) {
      fail("IOException should not have been thrown.");
    }
  }

  @Test
  public void testFlushQuietly_withException() {
    try {
      doThrow(new IOException("Test IOException")).when(mockFlushable).flush();
      Flushables.flushQuietly(mockFlushable);
      verify(mockLogger).log(eq(Level.SEVERE), anyString(), any(IOException.class));
    } catch (IOException e) {
      fail("IOException should not have been thrown.");
    }
  }
}