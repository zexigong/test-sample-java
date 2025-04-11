package org.mockito.internal.matchers;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class OrTest {

    @Test
    void testMatchesBothTrue() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.matches(any())).thenReturn(true);
        when(matcher2.matches(any())).thenReturn(true);

        Or orMatcher = new Or(matcher1, matcher2);
        assertTrue(orMatcher.matches(new Object()));
    }

    @Test
    void testMatchesFirstTrueSecondFalse() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.matches(any())).thenReturn(true);
        when(matcher2.matches(any())).thenReturn(false);

        Or orMatcher = new Or(matcher1, matcher2);
        assertTrue(orMatcher.matches(new Object()));
    }

    @Test
    void testMatchesFirstFalseSecondTrue() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.matches(any())).thenReturn(false);
        when(matcher2.matches(any())).thenReturn(true);

        Or orMatcher = new Or(matcher1, matcher2);
        assertTrue(orMatcher.matches(new Object()));
    }

    @Test
    void testMatchesBothFalse() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.matches(any())).thenReturn(false);
        when(matcher2.matches(any())).thenReturn(false);

        Or orMatcher = new Or(matcher1, matcher2);
        assertFalse(orMatcher.matches(new Object()));
    }

    @Test
    void testTypeBothAssignable() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.type()).thenReturn(String.class);
        when(matcher2.type()).thenReturn(Object.class);

        Or orMatcher = new Or(matcher1, matcher2);
        assertTrue(orMatcher.type().equals(Object.class));
    }

    @Test
    void testTypeNoneAssignable() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.type()).thenReturn(Integer.class);
        when(matcher2.type()).thenReturn(String.class);

        Or orMatcher = new Or(matcher1, matcher2);
        assertTrue(orMatcher.type().equals(Void.class));
    }

    @Test
    void testToString() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.toString()).thenReturn("matcher1");
        when(matcher2.toString()).thenReturn("matcher2");

        Or orMatcher = new Or(matcher1, matcher2);
        assertTrue(orMatcher.toString().equals("or(matcher1, matcher2)"));
    }
}