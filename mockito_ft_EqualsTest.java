/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualsTest {

    @Test
    public void shouldUseArgumentToStringIfPresent() {
        assertEquals("\"Test\"",
                new Equals("Test").toString());
    }

    @Test
    public void shouldUseBracketsIfArgumentToStringIsAbsent() {
        assertEquals("null", new Equals(null).toString());
    }

    @Test
    public void shouldMatchPrimitives() {
        assertTrue(new Equals(10).matches(10));
        assertFalse(new Equals(10).matches(20));
        assertFalse(new Equals(10).matches(null));
        assertFalse(new Equals(null).matches(10));
    }

    @Test
    public void shouldMatchObjects() {
        assertTrue(new Equals("Test").matches("Test"));
        assertFalse(new Equals("Test").matches("Test 2"));
        assertFalse(new Equals("Test").matches(null));
        assertFalse(new Equals(null).matches("Test"));
    }

    @Test
    public void shouldCompareToNull() {
        assertThat(new Equals(null)).isEqualTo(new Equals(null));
        assertThat(new Equals(null)).isNotEqualTo(new Equals("Test"));
        assertThat(new Equals("Test")).isNotEqualTo(new Equals(null));
    }

    @Test
    public void shouldCompareToTheSameObject() {
        assertThat(new Equals("Test")).isEqualTo(new Equals("Test"));
        assertThat(new Equals("Test")).isNotEqualTo(new Equals("Test 2"));
    }
}