/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class OrTest {
    @Test
    public void should_match_if_either_matcher_matches() throws Exception {
        ArgumentMatcher<Object> matcher1 = (argument) -> argument.equals("foo");
        ArgumentMatcher<Object> matcher2 = (argument) -> argument.equals("bar");
        Or or = new Or(matcher1, matcher2);

        assertThat(or.matches("foo")).isTrue();
        assertThat(or.matches("bar")).isTrue();
        assertThat(or.matches("baz")).isFalse();
    }

    @Test
    public void should_describe_matcher_type() throws Exception {
        ArgumentMatcher<Object> matcher1 = (argument) -> argument.equals("foo");
        ArgumentMatcher<Object> matcher2 = (argument) -> argument.equals("bar");
        Or or = new Or(matcher1, matcher2);

        assertThat(or.type()).isEqualTo(Void.class);
    }

    @Test
    public void should_describe_or() throws Exception {
        ArgumentMatcher<Object> matcher1 = (argument) -> argument.equals("foo");
        ArgumentMatcher<Object> matcher2 = (argument) -> argument.equals("bar");
        Or or = new Or(matcher1, matcher2);

        assertThat(or.toString()).isEqualTo("or(" + matcher1 + ", " + matcher2 + ")");
    }
}