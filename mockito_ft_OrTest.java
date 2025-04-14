/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AbstractBooleanAssert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.MatcherToString;

public class OrTest {

    @Test
    public void should_return_match_if_one_matcher_matches() {
        // given
        ArgumentMatcher<Object> first = new Any();
        ArgumentMatcher<Object> second = new Equals(100);
        Or matcher = new Or(first, second);

        // when
        AbstractBooleanAssert<?> booleanAssert = assertThat(matcher.matches(100));

        // then
        booleanAssert.isTrue();
    }

    @Test
    public void should_not_return_match_if_none_matcher_matches() {
        // given
        ArgumentMatcher<Object> first = new Equals(200);
        ArgumentMatcher<Object> second = new Equals(100);
        Or matcher = new Or(first, second);

        // when
        AbstractBooleanAssert<?> booleanAssert = assertThat(matcher.matches(300));

        // then
        booleanAssert.isFalse();
    }

    @Test
    public void should_have_friendly_toString() {
        // given
        ArgumentMatcher<Object> first = new Equals(10);
        ArgumentMatcher<Object> second = new Equals(20);
        Or matcher = new Or(first, second);

        // when
        String description = MatcherToString.toString(matcher);

        // then
        assertThat(description).isEqualTo("or(10, 20)");
    }
}