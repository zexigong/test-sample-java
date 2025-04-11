package org.mockito.internal.matchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.exceptions.Reporter.noArgumentValueWasCaptured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CapturingMatcherTest {

    private CapturingMatcher<String> capturingMatcher;

    @BeforeEach
    public void setUp() {
        capturingMatcher = new CapturingMatcher<>(String.class);
    }

    @Test
    public void testMatchesWithNullArgument() {
        assertTrue(capturingMatcher.matches(null));
    }

    @Test
    public void testMatchesWithMatchingArgument() {
        assertTrue(capturingMatcher.matches("someString"));
    }

    @Test
    public void testMatchesWithNonMatchingArgument() {
        assertFalse(capturingMatcher.matches(123));
    }

    @Test
    public void testCaptureFrom() {
        capturingMatcher.captureFrom("first");
        capturingMatcher.captureFrom("second");
        List<String> values = capturingMatcher.getAllValues();
        assertEquals(2, values.size());
        assertEquals("first", values.get(0));
        assertEquals("second", values.get(1));
    }

    @Test
    public void testGetLastValue() {
        capturingMatcher.captureFrom("first");
        capturingMatcher.captureFrom("second");
        assertEquals("second", capturingMatcher.getLastValue());
    }

    @Test
    public void testGetLastValueThrowsExceptionWhenNoValuesCaptured() {
        Exception exception = assertThrows(Exception.class, () -> capturingMatcher.getLastValue());
        assertEquals(noArgumentValueWasCaptured().getMessage(), exception.getMessage());
    }

    @Test
    public void testGetAllValuesReturnsEmptyListWhenNoValuesCaptured() {
        List<String> values = capturingMatcher.getAllValues();
        assertTrue(values.isEmpty());
    }

    @Test
    public void testType() {
        assertEquals(String.class, capturingMatcher.type());
    }

    @Test
    public void testToString() {
        assertEquals("<Capturing argument: String>", capturingMatcher.toString());
    }
}