/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SameTest {

    @Test
    void shouldMatchSameObject() {
        String o = "test";
        assertThat(new Same(o).matches(o)).isTrue();
    }

    @Test
    void shouldNotMatchSameObject() {
        String o = "test";
        assertThat(new Same(o).matches("test")).isFalse();
    }

    @Test
    void shouldNotMatchNull() {
        assertThat(new Same(null).matches("test")).isFalse();
    }

    @Test
    void shouldMatchNull() {
        assertThat(new Same(null).matches(null)).isTrue();
    }
}