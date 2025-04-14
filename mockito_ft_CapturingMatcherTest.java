/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.internal.exceptions.Reporter.noArgumentValueWasCaptured;

import java.util.List;

import org.junit.jupiter.api.Test;

class CapturingMatcherTest {

    private final CapturingMatcher<String> m = new CapturingMatcher<>(String.class);

    @Test
    void shouldCaptureNull() {
        m.captureFrom(null);
        assertThat(m.getLastValue()).isNull();
    }

    @Test
    void shouldNotCaptureNullWhenNoArgumentValueWasCaptured() {
        assertThrows(
                IllegalStateException.class,
                () -> m.getLastValue(),
                noArgumentValueWasCaptured().getMessage());
    }

    @Test
    void shouldCaptureValues() {
        m.captureFrom("foo");
        m.captureFrom("bar");
        m.captureFrom("baz");

        assertThat(m.getLastValue()).isEqualTo("baz");
    }

    @Test
    void shouldPullAllValues() {
        m.captureFrom("foo");
        m.captureFrom("bar");
        m.captureFrom("baz");

        List<String> allValues = m.getAllValues();
        assertThat(allValues).hasSize(3);
        assertThat(allValues.get(0)).isEqualTo("foo");
        assertThat(allValues.get(1)).isEqualTo("bar");
        assertThat(allValues.get(2)).isEqualTo("baz");
    }
}