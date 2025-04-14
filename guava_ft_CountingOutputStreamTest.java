/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.io;

import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.TestCase;

/**
 * Unit test for {@link CountingOutputStream}.
 *
 * @author Chris Nokleberg
 */
public class CountingOutputStreamTest extends TestCase {

  private static final String STRING = "foo";

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();
  private final CountingOutputStream countingOut = new CountingOutputStream(out);

  public void testBasics() throws IOException {
    assertThat(countingOut.getCount()).isEqualTo(0);

    countingOut.write(STRING.getBytes(UTF_8), 0, 2);
    assertThat(countingOut.getCount()).isEqualTo(2);

    countingOut.write(STRING.getBytes(UTF_8));
    assertThat(countingOut.getCount()).isEqualTo(5);

    countingOut.write('a');
    assertThat(countingOut.getCount()).isEqualTo(6);

    countingOut.flush();
    assertThat(countingOut.getCount()).isEqualTo(6);

    countingOut.close();
    assertThat(countingOut.getCount()).isEqualTo(6);
  }

  public void testNullOutputStream() {
    try {
      new CountingOutputStream(null);
      fail();
    } catch (NullPointerException expected) {
    }
  }
}