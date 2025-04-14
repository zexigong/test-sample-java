/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.matchers.text.MatchersPrinter.extending;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

public class AndTest {

    @Test
    void shouldMatch() {
        ArgumentMatcher<?> m = new And(new InstanceOf(Integer.class), new InstanceOf(Integer.class));
        assertThat(m.matches(100)).isTrue();
    }

    @Test
    void shouldNotMatch() {
        ArgumentMatcher<?> m = new And(new InstanceOf(Integer.class), new InstanceOf(Double.class));
        assertThat(m.matches(100)).isFalse();
    }

    @Test
    void shouldPrint() {
        ArgumentMatcher<?> m = new And(new InstanceOf(Integer.class), new InstanceOf(Integer.class));
        assertThat(m.toString()).isEqualTo("and(isA(java.lang.Integer), isA(java.lang.Integer))");
    }

    @Test
    void matchesWhenBothMatchersAreOfTheSameType() {
        ArgumentMatcher<Integer> isInteger = new InstanceOf<>(Integer.class);
        And and = new And(isInteger, isInteger);

        assertThat(and.type()).isEqualTo(Integer.class);
    }

    @Test
    void matchesWhenOneMatcherIsASuperType() {
        ArgumentMatcher<Number> isNumber = new InstanceOf<>(Number.class);
        ArgumentMatcher<Integer> isInteger = new InstanceOf<>(Integer.class);
        And and = new And(isNumber, isInteger);

        assertThat(and.type()).isEqualTo(Number.class);
    }

    @Test
    void matchesWhenOneMatcherIsASubType() {
        ArgumentMatcher<Number> isNumber = new InstanceOf<>(Number.class);
        ArgumentMatcher<Integer> isInteger = new InstanceOf<>(Integer.class);
        And and = new And(isInteger, isNumber);

        assertThat(and.type()).isEqualTo(Number.class);
    }

    @Test
    void doesNotMatchWhenMatchersAreNotCovariant() {
        ArgumentMatcher<Number> isNumber = new InstanceOf<>(Number.class);
        ArgumentMatcher<List> isList = new InstanceOf<>(List.class);
        And and = new And(isNumber, isList);

        assertThat(and.type()).isEqualTo(ArgumentMatcher.super.type());
    }

    @Test
    void matchesWhenBothMatchersAreOfTheSameTypeAndHaveDifferentTypeExtending() {
        ArgumentMatcher<Integer> isInteger1 = new InstanceOf<>(Integer.class, extending("1"));
        ArgumentMatcher<Integer> isInteger2 = new InstanceOf<>(Integer.class, extending("2"));
        And and = new And(isInteger1, isInteger2);

        assertThat(and.type()).isEqualTo(Integer.class);
    }
}