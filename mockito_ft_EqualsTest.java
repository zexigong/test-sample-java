/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EqualsTest {

    @Test
    public void should_match_equal_objects() {
        assertThat(new Equals("test")).accepts("test").rejects("not test");
    }

    @Test
    public void should_match_null_values() {
        assertThat(new Equals(null)).accepts((Object) null).rejects("not null");
    }

    @Test
    public void should_print_as_string() {
        assertThat(new Equals("test")).hasToString("\"test\"");
    }

    @Test
    public void should_not_equal_object_of_different_class() {
        assertThat(new Equals("test")).isNotEqualTo(new Object());
    }

    @Test
    public void should_equal_to_self() {
        Equals equals = new Equals("test");
        assertThat(equals).isEqualTo(equals);
    }

    @Test
    public void should_equal_to_equal_objects() {
        assertThat(new Equals("test")).isEqualTo(new Equals("test"));
    }

    @Test
    public void should_not_equal_to_different_objects() {
        assertThat(new Equals("test")).isNotEqualTo(new Equals("different"));
    }

    @Test
    public void should_not_equal_to_null() {
        assertThat(new Equals("test")).isNotEqualTo(null);
    }

    @Test
    public void should_have_1_as_hash_code() {
        assertThat(new Equals("test").hashCode()).isEqualTo(1);
    }

    @Test
    public void should_return_wanted() {
        assertThat(new Equals("test").getWanted()).isEqualTo("test");
    }

    @Test
    public void should_have_type_when_wanted_is_not_null() {
        assertThat(new Equals("test").type()).isEqualTo(String.class);
    }

    @Test
    public void should_not_have_type_when_wanted_is_null() {
        assertThat(new Equals(null).type()).isNull();
    }

    @Test
    public void should_return_type_matches_when_wanted_is_not_null_and_target_is_equal_to_wanted() {
        assertThat(new Equals("test").typeMatches("another test")).isTrue();
    }

    @Test
    public void should_return_type_not_matches_when_wanted_is_not_null_and_target_is_not_equal_to_wanted() {
        assertThat(new Equals("test").typeMatches(1)).isFalse();
    }

    @Test
    public void should_return_type_not_matches_when_wanted_is_null() {
        assertThat(new Equals(null).typeMatches("test")).isFalse();
    }

    @Test
    public void should_return_type_not_matches_when_target_is_null() {
        assertThat(new Equals("test").typeMatches(null)).isFalse();
    }

    @Test
    public void should_print_with_type() {
        assertThat(new Equals("test").toStringWithType("classType")).isEqualTo("(classType) \"test\"");
    }
}