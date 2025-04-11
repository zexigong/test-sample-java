package org.mockito.internal.matchers;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AndTest {

    @Test
    void testMatchesBothTrue() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.matches(any())).thenReturn(true);
        when(matcher2.matches(any())).thenReturn(true);

        And andMatcher = new And(matcher1, matcher2);

        assertTrue(andMatcher.matches(new Object()));
    }

    @Test
    void testMatchesOneFalse() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.matches(any())).thenReturn(true);
        when(matcher2.matches(any())).thenReturn(false);

        And andMatcher = new And(matcher1, matcher2);

        assertFalse(andMatcher.matches(new Object()));
    }

    @Test
    void testMatchesBothFalse() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.matches(any())).thenReturn(false);
        when(matcher2.matches(any())).thenReturn(false);

        And andMatcher = new And(matcher1, matcher2);

        assertFalse(andMatcher.matches(new Object()));
    }

    @Test
    void testTypeBothAssignable() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.type()).thenReturn(Number.class);
        when(matcher2.type()).thenReturn(Integer.class);

        And andMatcher = new And(matcher1, matcher2);

        assertEquals(Number.class, andMatcher.type());
    }

    @Test
    void testTypeOneAssignable() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.type()).thenReturn(String.class);
        when(matcher2.type()).thenReturn(CharSequence.class);

        And andMatcher = new And(matcher1, matcher2);

        assertEquals(CharSequence.class, andMatcher.type());
    }

    @Test
    void testToString() {
        ArgumentMatcher<Object> matcher1 = mock(ArgumentMatcher.class);
        ArgumentMatcher<Object> matcher2 = mock(ArgumentMatcher.class);

        when(matcher1.toString()).thenReturn("matcher1");
        when(matcher2.toString()).thenReturn("matcher2");

        And andMatcher = new And(matcher1, matcher2);

        assertEquals("and(matcher1, matcher2)", andMatcher.toString());
    }
}