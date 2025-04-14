/*
 * Copyright (C) 2013 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.io;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CharSequenceReader}.
 *
 * @author Colin Decker
 */
@RunWith(JUnit4.class)
public class CharSequenceReaderTest {

  private static final String STRING = "abcdef";
  private static final CharSequence CHAR_SEQUENCE = new StringBuilder(STRING);

  private CharSequenceReader in;

  @Before
  public void setUp() {
    in = new CharSequenceReader(CHAR_SEQUENCE);
  }

  @After
  public void tearDown() throws IOException {
    in.close();
  }

  @Test
  public void read() throws IOException {
    assertThat((char) in.read()).isEqualTo('a');
    assertThat((char) in.read()).isEqualTo('b');
    assertThat((char) in.read()).isEqualTo('c');
    assertThat((char) in.read()).isEqualTo('d');
    assertThat((char) in.read()).isEqualTo('e');
    assertThat((char) in.read()).isEqualTo('f');
    assertThat(in.read()).isEqualTo(-1);
  }

  @Test
  public void read_withCharArray() throws IOException {
    char[] buf = new char[4];
    assertThat(in.read(buf)).isEqualTo(4);
    assertThat(buf).isEqualTo(new char[] {'a', 'b', 'c', 'd'});

    assertThat(in.read(buf)).isEqualTo(2);
    assertThat(buf).isEqualTo(new char[] {'e', 'f', 'c', 'd'});

    assertThat(in.read(buf)).isEqualTo(-1);
  }

  @Test
  public void skip() throws IOException {
    assertThat(in.skip(3)).isEqualTo(3L);
    assertThat((char) in.read()).isEqualTo('d');
    in.skip(Long.MAX_VALUE);
    assertThat(in.read()).isEqualTo(-1);
  }

  @Test
  public void markAndReset() throws IOException {
    assertThat((char) in.read()).isEqualTo('a');
    in.mark(3);
    assertThat((char) in.read()).isEqualTo('b');
    assertThat((char) in.read()).isEqualTo('c');
    in.reset();
    assertThat((char) in.read()).isEqualTo('b');
  }

  @Test
  public void markWithoutReadAheadLimit() throws IOException {
    in.mark(0);
    assertThat((char) in.read()).isEqualTo('a');
    in.reset();
    assertThat((char) in.read()).isEqualTo('a');
  }

  @Test
  public void markAndReset_withoutRead() throws IOException {
    in.mark(3);
    in.reset();
    assertThat((char) in.read()).isEqualTo('a');
  }

  @Test
  public void close() throws IOException {
    assertThat((char) in.read()).isEqualTo('a');
    in.close();
    assertThrows(IOException.class, () -> in.read());
  }
}