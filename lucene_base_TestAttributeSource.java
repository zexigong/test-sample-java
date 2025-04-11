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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;

public class TestAttributeSource {

  private AttributeSource attributeSource;
  private static class TestAttributeImpl extends AttributeImpl implements TestAttribute {
    private String value;
    
    @Override
    public void clear() {
      value = null;
    }
    
    @Override
    public void end() {
      value = "end";
    }
    
    @Override
    public void copyTo(AttributeImpl target) {
      ((TestAttributeImpl) target).value = this.value;
    }
    
    @Override
    public void reflectWith(AttributeReflector reflector) {
      reflector.reflect(TestAttribute.class, "value", value);
    }
    
    @Override
    public AttributeImpl clone() {
      TestAttributeImpl clone = new TestAttributeImpl();
      clone.value = this.value;
      return clone;
    }
    
    @Override
    public boolean equals(Object other) {
      if (this == other) return true;
      if (!(other instanceof TestAttributeImpl)) return false;
      TestAttributeImpl o = (TestAttributeImpl) other;
      return value == null ? o.value == null : value.equals(o.value);
    }
    
    @Override
    public int hashCode() {
      return value == null ? 0 : value.hashCode();
    }
  }
  
  private interface TestAttribute extends Attribute {}

  @Before
  public void setUp() {
    attributeSource = new AttributeSource();
  }

  @Test
  public void testAddAttribute() {
    TestAttribute attr = attributeSource.addAttribute(TestAttribute.class);
    assertNotNull(attr);
    assertTrue(attributeSource.hasAttribute(TestAttribute.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddAttributeInvalid() {
    attributeSource.addAttribute(String.class);
  }

  @Test
  public void testHasAttributes() {
    assertFalse(attributeSource.hasAttributes());
    attributeSource.addAttribute(TestAttribute.class);
    assertTrue(attributeSource.hasAttributes());
  }

  @Test
  public void testGetAttribute() {
    attributeSource.addAttribute(TestAttribute.class);
    TestAttribute attr = attributeSource.getAttribute(TestAttribute.class);
    assertNotNull(attr);
  }

  @Test
  public void testCloneAttributes() {
    attributeSource.addAttribute(TestAttribute.class);
    AttributeSource clone = attributeSource.cloneAttributes();
    assertEquals(attributeSource, clone);
  }

  @Test
  public void testReflectAsString() {
    attributeSource.addAttribute(TestAttribute.class);
    String reflection = attributeSource.reflectAsString(false);
    assertEquals("value=null", reflection);
  }

  @Test
  public void testClearAttributes() {
    TestAttributeImpl attr = attributeSource.addAttribute(TestAttributeImpl.class);
    attr.value = "test";
    attributeSource.clearAttributes();
    assertNull(attr.value);
  }

  @Test
  public void testEndAttributes() {
    TestAttributeImpl attr = attributeSource.addAttribute(TestAttributeImpl.class);
    attributeSource.endAttributes();
    assertEquals("end", attr.value);
  }

  @Test
  public void testRemoveAllAttributes() {
    attributeSource.addAttribute(TestAttribute.class);
    attributeSource.removeAllAttributes();
    assertFalse(attributeSource.hasAttributes());
  }

  @Test
  public void testCaptureAndRestoreState() {
    TestAttributeImpl attr = attributeSource.addAttribute(TestAttributeImpl.class);
    attr.value = "test";
    AttributeSource.State state = attributeSource.captureState();
    attr.value = "modified";
    attributeSource.restoreState(state);
    assertEquals("test", attr.value);
  }

  @Test
  public void testEqualsAndHashCode() {
    AttributeSource source1 = new AttributeSource();
    AttributeSource source2 = new AttributeSource();
    assertEquals(source1, source2);
    assertEquals(source1.hashCode(), source2.hashCode());

    source1.addAttribute(TestAttribute.class);
    source2.addAttribute(TestAttribute.class);
    assertEquals(source1, source2);
    assertEquals(source1.hashCode(), source2.hashCode());

    source2.addAttributeImpl(new TestAttributeImpl());
    assertNotEquals(source1, source2);
    assertNotEquals(source1.hashCode(), source2.hashCode());
  }

  @Test
  public void testToString() {
    attributeSource.addAttribute(TestAttribute.class);
    String str = attributeSource.toString();
    assertTrue(str.contains("AttributeSource@"));
    assertTrue(str.contains("value=null"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRestoreStateInvalid() {
    AttributeSource source = new AttributeSource();
    source.addAttribute(TestAttribute.class);
    AttributeSource.State state = source.captureState();

    AttributeSource target = new AttributeSource();
    target.restoreState(state);
  }
}