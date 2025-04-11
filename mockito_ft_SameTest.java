/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.util.StringUtil.join;

import org.junit.jupiter.api.Test;

class SameTest {

    @Test
    void shouldMatchOnlySameObject() {
        String s = "test";
        Same same = new Same(s);

        assertThat(same.matches(s)).isTrue();
        assertThat(same.matches("test")).isFalse();
    }

    @Test
    void shouldPrintTheSameForNull() {
        Same same = new Same(null);
        assertThat(same).hasToString("same(null)");
    }

    @Test
    void shouldPrintTheSameForString() {
        Same same = new Same("x");
        assertThat(same).hasToString("same(\"x\")");
    }

    @Test
    void shouldPrintTheSameForChar() {
        Same same = new Same('x');
        assertThat(same).hasToString("same('x')");
    }

    @Test
    void shouldPrintTheSameForLong() {
        Same same = new Same(100L);
        assertThat(same).hasToString("same(100L)");
    }

    @Test
    void shouldPrintTheSameForDouble() {
        Same same = new Same(100.0d);
        assertThat(same).hasToString("same(100.0d)");
    }

    @Test
    void shouldPrintTheSameForFloat() {
        Same same = new Same(100.0f);
        assertThat(same).hasToString("same(100.0f)");
    }

    @Test
    void shouldPrintTheSameForShort() {
        Same same = new Same((short) 100);
        assertThat(same).hasToString("same((short) 100)");
    }

    @Test
    void shouldPrintTheSameForByte() {
        Same same = new Same((byte) 0x2F);
        assertThat(same).hasToString("same((byte) 0x2F)");
    }

    @Test
    void shouldPrintTheSameForArrays() {
        Same same = new Same(new Object[] {1, 2});
        assertThat(same).hasToString("same([1, 2])");
    }

    @Test
    void shouldPrintTheSameForCollections() {
        Same same = new Same(join("x", "y"));
        assertThat(same).hasToString("same(\"x\", \"y\")");
    }

    @Test
    void typeIsCorrect() {
        Same same = new Same("x");
        assertThat(same.type()).isEqualTo(String.class);
    }

    @Test
    void typeIsCorrectWhenNull() {
        Same same = new Same(null);
        assertThat(same.type()).isEqualTo(Void.class);
    }
}