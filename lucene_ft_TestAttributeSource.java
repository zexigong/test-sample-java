/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.util;

import java.util.Iterator;
import org.apache.lucene.util.AttributeSource.State;
import org.apache.lucene.util.LuceneTestCase.SuppressCodecs;
import org.junit.Test;

@SuppressCodecs("Direct")
public class TestAttributeSource extends LuceneTestCase {

  @Test
  public void testCaptureState() throws Exception {
    AttributeSource src = new AttributeSource();
    TermToBytesRefAttribute termAtt = src.addAttribute(TermToBytesRefAttribute.class);
    TypeAttribute typeAtt = src.addAttribute(TypeAttribute.class);

    termAtt.setBytesRef(new BytesRef("TestTerm"));
    typeAtt.setType("TestType");

    State state = src.captureState();
    termAtt.setBytesRef(new BytesRef("AnotherTestTerm"));
    typeAtt.setType("AnotherTestType");
    src.restoreState(state);
    assertEquals(termAtt.getBytesRef(), new BytesRef("TestTerm"));
    assertEquals(typeAtt.type(), "TestType");

    // check that the restoreState() call didn't affect the captured state
    termAtt.setBytesRef(new BytesRef("AnotherTestTerm"));
    typeAtt.setType("AnotherTestType");
    src.restoreState(state);
    assertEquals(termAtt.getBytesRef(), new BytesRef("TestTerm"));
    assertEquals(typeAtt.type(), "TestType");
  }

  @Test
  public void testCloneAttributes() throws Exception {
    AttributeSource src = new AttributeSource();
    TermToBytesRefAttribute termAtt = src.addAttribute(TermToBytesRefAttribute.class);
    TypeAttribute typeAtt = src.addAttribute(TypeAttribute.class);

    termAtt.setBytesRef(new BytesRef("TestTerm"));
    typeAtt.setType("TestType");

    AttributeSource clone = src.cloneAttributes();
    TermToBytesRefAttribute clonedTermAtt = clone.getAttribute(TermToBytesRefAttribute.class);
    TypeAttribute clonedTypeAtt = clone.getAttribute(TypeAttribute.class);
    assertNotNull(clonedTermAtt);
    assertNotNull(clonedTypeAtt);
    assertEquals(clonedTermAtt.getBytesRef(), new BytesRef("TestTerm"));
    assertEquals(clonedTypeAtt.type(), "TestType");

    // test copyTo() before/after
    termAtt.setBytesRef(new BytesRef("AnotherTestTerm"));
    typeAtt.setType("AnotherTestType");
    src.copyTo(clone);
    assertEquals(clonedTermAtt.getBytesRef(), new BytesRef("AnotherTestTerm"));
    assertEquals(clonedTypeAtt.type(), "AnotherTestType");
    termAtt.setBytesRef(new BytesRef("TestTerm"));
    typeAtt.setType("TestType");
    clone.copyTo(src);
    assertEquals(termAtt.getBytesRef(), new BytesRef("AnotherTestTerm"));
    assertEquals(typeAtt.type(), "AnotherTestType");
  }

  @Test
  public void testInvalidArguments() {
    AttributeSource src = new AttributeSource();
    try {
      src.addAttribute(Iterator.class);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      // pass
    }
    try {
      src.addAttribute(Iterable.class);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException iae) {
      // pass
    }
  }

  @Test
  public void testGetAttribute() throws Exception {
    final AttributeSource src = new AttributeSource();
    assertNull(src.getAttribute(TermToBytesRefAttribute.class));
    src.addAttribute(TermToBytesRefAttribute.class);
    assertNotNull(src.getAttribute(TermToBytesRefAttribute.class));
  }

  @Test
  public void testAttributeFactory() throws Exception {
    final AttributeSource src = new AttributeSource();
    assertEquals(
        AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY.getClass(), src.getAttributeFactory().getClass());
    final AttributeSource src2 = new AttributeSource(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
    assertEquals(
        AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY.getClass(),
        src2.getAttributeFactory().getClass());
  }

  @Test
  public void testDefaultAttributeFactory() throws Exception {
    final AttributeSource src = new AttributeSource();
    assertEquals(
        AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY.getClass(), src.getAttributeFactory().getClass());
    final AttributeSource src2 = new AttributeSource(AttributeSource.DEFAULT_ATTRIBUTE_FACTORY);
    assertEquals(
        AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY.getClass(),
        src2.getAttributeFactory().getClass());
  }

  @Test
  public void testCustomAttributeFactory() throws Exception {
    final AttributeSource src =
        new AttributeSource(
            new AttributeFactory() {
              @Override
              public AttributeImpl createAttributeInstance(Class<? extends Attribute> attClass) {
                return null;
              }
            });
    assertTrue(!(src.getAttributeFactory() instanceof AttributeFactory.StaticImplementationAttributeFactory<?>));
    assertTrue(!(src.getAttributeFactory() instanceof AttributeFactory.CachingAttributeFactory));
  }

  @Test
  public void testClearAttributes() throws Exception {
    final AttributeSource src = new AttributeSource();
    final TermToBytesRefAttribute termAtt = src.addAttribute(TermToBytesRefAttribute.class);
    final TypeAttribute typeAtt = src.addAttribute(TypeAttribute.class);
    termAtt.setBytesRef(new BytesRef("TestTerm"));
    typeAtt.setType("TestType");
    src.clearAttributes();
    assertEquals(termAtt.getBytesRef().length, 0);
    assertNull(typeAtt.type());
  }

  @Test
  public void testRemoveAllAttributes() throws Exception {
    final AttributeSource src = new AttributeSource();
    src.addAttribute(TermToBytesRefAttribute.class);
    src.addAttribute(TypeAttribute.class);
    src.removeAllAttributes();
    assertEquals(src.hasAttributes(), false);
  }
}