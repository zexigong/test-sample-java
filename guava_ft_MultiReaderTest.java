/*
 * Copyright (C) 2011 The Guava Authors
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

import static com.google.common.collect.Iterables.transform;
import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Unit test for {@link MultiReader}.
 *
 * @author Bin Zhu
 */
@GwtIncompatible // MultiReader
public class MultiReaderTest extends IoTestCase {

  // first part of test string
  private static final String STRING_FIRST_PART =
      "When forty winters shall beseige thy brow,\n"
          + "And dig deep trenches in thy beauty's field,\n"
          + "Thy youth's proud livery, so gazed on now,\n"
          + "Will be a tattered weed, of small worth held:\n"
          + "Then being asked, where all thy beauty lies,\n"
          + "Where all the treasure of thy lusty days;\n"
          + "To say, within thine own deep sunken eyes,\n"
          + "Were an all-eating shame, and thriftless praise.\n"
          + "How much more praise deserved thy beauty's use,\n"
          + "If thou couldst answer 'This fair child of mine\n"
          + "Shall sum my count, and make my old excuse,'\n"
          + "Proving his beauty by succession thine!\n";

  // second part of test string
  private static final String STRING_SECOND_PART =
      "This were to be new made when thou art old,\n"
          + "And see thy blood warm when thou feel'st it cold.\n";

  // whole test string
  private static final String STRING_WHOLE = STRING_FIRST_PART + STRING_SECOND_PART;

  private static final List<String> STRING_LIST = Arrays.asList(STRING_FIRST_PART, STRING_SECOND_PART);

  private static final Function<String, CharSource> STRING_TO_CHARSOURCE =
      new Function<String, CharSource>() {
        @Override
        public CharSource apply(String from) {
          return CharSource.wrap(from);
        }
      };

  // test reading
  @Test
  public void testRead() throws IOException {
    Reader reader = new MultiReader(transform(STRING_LIST, STRING_TO_CHARSOURCE).iterator());
    assertThat(ReaderAsString.read(reader)).isEqualTo(STRING_WHOLE);
    reader.close();
  }
}