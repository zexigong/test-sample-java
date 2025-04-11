/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EqualityTest {

    @Test
    void shouldSayEqualObjectsAreEqual() {
        assertThat(Equality.areEqual(1, 1)).isTrue();
        assertThat(Equality.areEqual(1.1, 1.1)).isTrue();
        assertThat(Equality.areEqual(new Object(), new Object())).isFalse();
        assertThat(Equality.areEqual(new Object(), null)).isFalse();
        assertThat(Equality.areEqual(null, new Object())).isFalse();
        assertThat(Equality.areEqual(null, null)).isTrue();
    }

    @Test
    void shouldSayEqualArraysAreEqual() {
        assertThat(Equality.areEqual(new int[] {1, 2}, new int[] {1, 2})).isTrue();
        assertThat(Equality.areEqual(new int[] {1, 2}, new int[] {2, 1})).isFalse();
        assertThat(Equality.areEqual(new int[] {1, 2}, new int[] {1, 2, 3})).isFalse();
        assertThat(Equality.areEqual(new int[] {1, 2}, new Object[] {1, 2})).isFalse();
    }

    @Test
    void shouldSayEqualMultidimArraysAreEqual() {
        assertThat(Equality.areEqual(new int[][] {{1, 2}}, new int[][] {{1, 2}})).isTrue();
        assertThat(Equality.areEqual(new int[][] {{1, 2}}, new int[][] {{2, 1}})).isFalse();
        assertThat(Equality.areEqual(new int[][] {{1, 2}}, new int[][] {{1, 2, 3}})).isFalse();
        assertThat(Equality.areEqual(new int[][] {{1, 2}}, new Object[][] {{1, 2}})).isFalse();
    }

    @Test
    void shouldSayEqualMultidimArraysAreEqualWhenArrayContainsNulls() {
        assertThat(Equality.areEqual(new int[][] {{1, 2}, null}, new int[][] {{1, 2}, null})).isTrue();
        assertThat(Equality.areEqual(new int[][] {{1, 2}, null}, new int[][] {{1, 2}})).isFalse();
    }
}