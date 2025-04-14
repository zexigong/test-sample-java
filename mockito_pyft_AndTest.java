package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;

class AndTest {

    private final ArgumentMatcher<Object> matcher1 = new Equals(1);
    private final ArgumentMatcher<Object> matcher2 = new Equals(2);

    @Test
    void shouldBeEqual() {
        // given
        ArgumentMatcher<Object> and = new And(matcher1, matcher2);

        // then
        assertThat(and.toString()).isEqualTo("and(1, 2)");
    }

    @Test
    void shouldNotMatch() {
        // given
        ArgumentMatcher<Object> and = new And(matcher1, matcher2);

        // then
        assertThat(and.matches(1)).isFalse();
        assertThat(and.matches(2)).isFalse();
    }

    @Test
    void shouldMatch() {
        // given
        ArgumentMatcher<Object> and = new And(new Equals(1), new Equals(1));

        // then
        assertThat(and.matches(1)).isTrue();
    }

    @Test
    void shouldThrowErrorForAndMatcher() {
        // given
        Foo mock = mock(Foo.class);

        // when
        when(mock.bar(argThat(new And(matcher1, matcher2)))).thenReturn(true);

        // then
        assertThatExceptionOfType(InvalidUseOfMatchersException.class).isThrownBy(() -> mock.bar(null))
                .withMessageContaining("You cannot use argument matchers outside of verification or stubbing");
    }

    @Test
    void typeOfFirstMatcherWhenFirstTypeIsAssignableFromSecondType() {
        // given
        ArgumentMatcher<String> m1 = new AssignableFromType(String.class);
        ArgumentMatcher<Object> m2 = new AssignableFromType(Object.class);

        // when
        ArgumentMatcher<Object> and = new And(m1, m2);

        // then
        assertThat(and.type()).isEqualTo(m1.type());
    }

    @Test
    void typeOfSecondMatcherWhenSecondTypeIsAssignableFromFirstType() {
        // given
        ArgumentMatcher<Object> m1 = new AssignableFromType(Object.class);
        ArgumentMatcher<String> m2 = new AssignableFromType(String.class);

        // when
        ArgumentMatcher<Object> and = new And(m1, m2);

        // then
        assertThat(and.type()).isEqualTo(m2.type());
    }

    @Test
    void defaultTypeWhenNeitherTypeIsAssignableFromTheOther() {
        // given
        ArgumentMatcher<String> m1 = new AssignableFromType(String.class);
        ArgumentMatcher<Integer> m2 = new AssignableFromType(Integer.class);

        // when
        ArgumentMatcher<Object> and = new And(m1, m2);

        // then
        assertThat(and.type()).isEqualTo(Void.class);
    }

    interface Foo {
        boolean bar(Object o);
    }

    static class AssignableFromType implements ArgumentMatcher<Object> {
        private final Class<?> type;

        AssignableFromType(Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean matches(Object argument) {
            return false;
        }

        @Override
        public Class<?> type() {
            return type;
        }
    }
}