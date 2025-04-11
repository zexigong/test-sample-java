/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.matchers.text.MatchersPrinter.toString;

import org.junit.jupiter.api.Test;

class AndTest {

    @Test
    void shouldNotHaveTypesWhenNoTypeMatchers() {
        assertThat(new And(new Equals("one"), new Equals("two")).type()).isEqualTo(Void.class);
    }

    @Test
    void shouldReturnTypeWhenOneTypeMatcher() {
        assertThat(new And(new TypeSafeMatcher<String>() {
            @Override
            public boolean matchesSafely(String item) {
                return false;
            }
        }, new Equals("two")).type()).isEqualTo(String.class);
    }

    @Test
    void shouldReturnTypeWhenTwoTypeMatchers() {
        assertThat(new And(new TypeSafeMatcher<String>() {
            @Override
            public boolean matchesSafely(String item) {
                return false;
            }
        }, new TypeSafeMatcher<CharSequence>() {
            @Override
            public boolean matchesSafely(CharSequence item) {
                return false;
            }
        }).type()).isEqualTo(CharSequence.class);
    }

    @Test
    void shouldNeverReturnVoidWhenMatcherWithVoidType() {
        assertThat(new And(new TypeSafeMatcher<Void>() {
            @Override
            public boolean matchesSafely(Void item) {
                return false;
            }
        }, new TypeSafeMatcher<String>() {
            @Override
            public boolean matchesSafely(String item) {
                return false;
            }
        }).type()).isEqualTo(String.class);
    }

    @Test
    void shouldNotReturnVoidWhenMatcherWithVoidType() {
        assertThat(new And(new TypeSafeMatcher<Void>() {
            @Override
            public boolean matchesSafely(Void item) {
                return false;
            }
        }, new Equals("two")).type()).isEqualTo(Void.class);
    }

    @Test
    void shouldNotReturnVoidWhenMatcherWithVoidTypeIsSecond() {
        assertThat(new And(new Equals("two"), new TypeSafeMatcher<Void>() {
            @Override
            public boolean matchesSafely(Void item) {
                return false;
            }
        }).type()).isEqualTo(Void.class);
    }

    @Test
    void shouldPrintAnd() {
        And and = new And(new Equals("one"), new Equals("two"));
        assertThat(toString(and)).isEqualTo("and(\"one\", \"two\")");
    }
}