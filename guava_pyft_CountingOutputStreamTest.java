/*
 * Copyright (C) 2007 The Guava Authors
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

import com.google.common.annotations.GwtIncompatible;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import junit.framework.TestCase;

/**
 * Unit test for {@link CountingOutputStream}.
 *
 * @author Chris Nokleberg
 */
@GwtIncompatible // CountingOutputStream
public class CountingOutputStreamTest extends TestCase {

  private CountingOutputStream counter;
  private OutputStream out;

  @Override
  protected void setUp() {
    out = new ByteArrayOutputStream();
    counter = new CountingOutputStream(out);
  }

  public void testWriteInt() throws IOException {
    assertThat(counter.getCount()).isEqualTo(0);
    counter.write(0);
    assertThat(counter.getCount()).isEqualTo(1);
    counter.write(0);
    assertThat(counter.getCount()).isEqualTo(2);
    counter.write(0);
    assertThat(counter.getCount()).isEqualTo(3);
  }

  public void testWrite() throws IOException {
    byte[] buf = new byte[10];
    assertThat(counter.getCount()).isEqualTo(0);
    counter.write(buf);
    assertThat(counter.getCount()).isEqualTo(10);
    counter.write(buf);
    assertThat(counter.getCount()).isEqualTo(20);
    counter.write(buf, 0, 1);
    assertThat(counter.getCount()).isEqualTo(21);
    counter.write(buf, 0, 0);
    assertThat(counter.getCount()).isEqualTo(21);
  }
}