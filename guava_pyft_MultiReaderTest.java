package com.google.common.io;

import static com.google.common.io.BaseEncoding.base16;
import static com.google.common.io.BaseEncoding.base32;
import static com.google.common.io.BaseEncoding.base32Hex;
import static com.google.common.io.BaseEncoding.base64;
import static com.google.common.io.BaseEncoding.base64Url;
import static com.google.common.io.BaseEncoding.base64UrlOmitPadding;
import static com.google.common.io.BaseEncoding.base64OmitPadding;
import static com.google.common.io.BaseEncoding.base64LenientPadding;
import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Bytes;
import com.google.common.testing.ArbitraryInstances;
import com.google.common.testing.NullPointerTester;
import com.google.common.testing.SerializableTester;
import com.google.common.testing.TestLogHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 * Unit tests for the BaseEncoding class.
 *
 * @author Louis Wasserman
 */
@GwtIncompatible // BaseEncoding
public class MultiReaderTest extends TestCase {
  public void testRead() throws IOException {
    String first = "abcde";
    String second = "fghij";
    String third = "klmno";
    String fourth = "pqrst";

    MultiReader reader =
        new MultiReader(
            ImmutableList.of(
                CharSource.wrap(first),
                CharSource.wrap(second),
                CharSource.wrap(third),
                CharSource.wrap(fourth))
                .iterator());

    char[] buf = new char[10];
    assertEquals(5, reader.read(buf, 0, 5));
    assertEquals("abcde", new String(buf, 0, 5));
    assertEquals(3, reader.read(buf, 0, 3));
    assertEquals("fgh", new String(buf, 0, 3));
    assertEquals(0, reader.read(buf, 0, 0));
    assertEquals(10, reader.read(buf, 0, 10));
    assertEquals("ij", new String(buf, 0, 2));
    assertEquals("klmno", new String(buf, 2, 5));
    assertEquals("pqrst", new String(buf, 7, 3));
    assertEquals(-1, reader.read(buf, 0, 10));
    assertEquals(-1, reader.read(buf, 0, 10));
  }

  public void testSkip() throws IOException {
    String first = "abcde";
    String second = "fghij";
    String third = "klmno";
    String fourth = "pqrst";

    MultiReader reader =
        new MultiReader(
            ImmutableList.of(
                CharSource.wrap(first),
                CharSource.wrap(second),
                CharSource.wrap(third),
                CharSource.wrap(fourth))
                .iterator());

    assertEquals(5, reader.skip(5));
    assertEquals('f', reader.read());
    assertEquals('g', reader.read());
    assertEquals(0, reader.skip(0));
    assertEquals('h', reader.read());
    assertEquals(7, reader.skip(7));
    assertEquals('p', reader.read());
    assertEquals('q', reader.read());
    assertEquals('r', reader.read());
    assertEquals('s', reader.read());
    assertEquals('t', reader.read());
    assertEquals(0, reader.skip(1));
  }
}