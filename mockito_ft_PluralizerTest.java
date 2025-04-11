/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PluralizerTest {

    @Test
    void pluralizes() {
        assertThat(Pluralizer.pluralize(1)).isEqualTo("1 time");
        assertThat(Pluralizer.pluralize(2)).isEqualTo("2 times");
    }

    @Test
    void wereExactlyXInteractions() {
        assertThat(Pluralizer.were_exactly_x_interactions(1)).isEqualTo("was exactly 1 interaction");
        assertThat(Pluralizer.were_exactly_x_interactions(5)).isEqualTo("were exactly 5 interactions");
    }
}