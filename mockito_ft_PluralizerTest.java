/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PluralizerTest {

    @Test
    void pluralize() {
        assertEquals("1 time", Pluralizer.pluralize(1));
        assertEquals("0 times", Pluralizer.pluralize(0));
        assertEquals("2 times", Pluralizer.pluralize(2));
    }

    @Test
    void were_exactly_x_interactions() {
        assertEquals("was exactly 1 interaction", Pluralizer.were_exactly_x_interactions(1));
        assertEquals("were exactly 0 interactions", Pluralizer.were_exactly_x_interactions(0));
        assertEquals("were exactly 2 interactions", Pluralizer.were_exactly_x_interactions(2));
    }
}