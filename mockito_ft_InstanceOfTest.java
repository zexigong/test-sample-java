/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class InstanceOfTest {

    private final InstanceOf matcher = new InstanceOf(List.class);

    @Test
    public void should_return_false_if_parameter_is_not_instance_of_class() {
        assertFalse(matcher.matches(new Object()));
    }

    @Test
    public void should_return_true_if_parameter_is_instance_of_class() {
        assertTrue(matcher.matches(new AlwaysEqualMatcher()));
    }

    @Test
    public void should_return_true_if_parameter_is_subclass_of_class() {
        assertTrue(matcher.matches(new MatcherWithEquals()));
    }

    @Test
    public void should_match_primitive_class() {
        assertThat(new InstanceOf(int.class).matches(1)).isTrue();
        assertThat(new InstanceOf(short.class).matches(1)).isTrue();
        assertThat(new InstanceOf(short.class).matches(1.0)).isFalse();
        assertThat(new InstanceOf(Integer.class).matches(1)).isTrue();
        assertThat(new InstanceOf(Integer.class).matches(1L)).isFalse();
        assertThat(new InstanceOf(Number.class).matches(1)).isTrue();
        assertThat(new InstanceOf(Number.class).matches(1L)).isTrue();
    }
}