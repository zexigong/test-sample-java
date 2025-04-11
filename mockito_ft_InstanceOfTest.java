/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.util.Primitives.isAssignableFromWrapper;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class InstanceOfTest {

    @Test
    public void shouldMatch() {
        // when
        boolean matched = new InstanceOf(Number.class).matches(1);
        // then
        assertThat(matched).isTrue();
    }

    @Test
    public void shouldNotMatch() {
        // when
        boolean matched = new InstanceOf(Number.class).matches("1");
        // then
        assertThat(matched).isFalse();
    }

    @Test
    public void shouldHaveDecentToStringWithNull() {
        // when
        String description = new InstanceOf(Number.class).toString();
        // then
        assertThat(description).isEqualTo("isA(java.lang.Number)");
    }

    @Test
    public void shouldHaveDecentToString() {
        // when
        String description = new InstanceOf(Number.class, "Number instance").toString();
        // then
        assertThat(description).isEqualTo("Number instance");
    }

    @Test
    public void shouldMatchPrimitive() {
        // when
        boolean matched = new InstanceOf(int.class).matches(1);
        // then
        assertThat(matched).isTrue();
    }

    @Test
    public void shouldMatchPrimitiveWrapper() {
        // when
        boolean matched = new InstanceOf(int.class).matches(Integer.valueOf(1));
        // then
        assertThat(matched).isTrue();
    }

    @Test
    public void shouldNotMatchPrimitive() {
        // when
        boolean matched = new InstanceOf(double.class).matches(1);
        // then
        assertThat(matched).isFalse();
    }

    @Test
    public void shouldNotMatchPrimitiveWrapper() {
        // when
        boolean matched = new InstanceOf(double.class).matches(Integer.valueOf(1));
        // then
        assertThat(matched).isFalse();
    }

    @Test
    public void shouldNotMatchNull() {
        // when
        boolean matched = new InstanceOf(Number.class).matches(null);
        // then
        assertThat(matched).isFalse();
    }

    @Test
    public void shouldMatchPrimitiveWrappers() {
        Map<Class<?>, Object> primitiveWrappers = new HashMap<>();
        primitiveWrappers.put(Boolean.class, true);
        primitiveWrappers.put(Character.class, '1');
        primitiveWrappers.put(Byte.class, (byte) 1);
        primitiveWrappers.put(Short.class, (short) 1);
        primitiveWrappers.put(Integer.class, 1);
        primitiveWrappers.put(Long.class, 1L);
        primitiveWrappers.put(Float.class, 1F);
        primitiveWrappers.put(Double.class, 1D);

        primitiveWrappers.forEach(
                (clazz, value) -> {
                    assertThat(new InstanceOf(clazz).matches(value)).isTrue();
                    assertThat(isAssignableFromWrapper(value.getClass(), clazz)).isTrue();
                });
    }

    @Test
    public void shouldMatchPrimitiveAndWrapper() {
        assertThat(isAssignableFromWrapper(Integer.class, int.class)).isTrue();
    }

    @Test
    public void shouldMatchWrapperAndPrimitive() {
        assertThat(isAssignableFromWrapper(int.class, Integer.class)).isTrue();
    }

    @Test
    public void shouldNotMatchPrimitive() {
        assertThat(isAssignableFromWrapper(int.class, double.class)).isFalse();
    }

    @Test
    public void shouldNotMatchWrapper() {
        assertThat(isAssignableFromWrapper(Integer.class, Double.class)).isFalse();
    }
}