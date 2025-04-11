package org.mockito.internal.matchers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SameTest {

    @Test
    void testMatchesSameObject() {
        Object obj = new Object();
        Same matcher = new Same(obj);
        
        assertTrue(matcher.matches(obj), "Expected the matcher to return true for the same object reference");
    }

    @Test
    void testMatchesDifferentObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same matcher = new Same(obj1);
        
        assertFalse(matcher.matches(obj2), "Expected the matcher to return false for different object references");
    }

    @Test
    void testMatchesNullWantedAndNullActual() {
        Same matcher = new Same(null);
        
        assertTrue(matcher.matches(null), "Expected the matcher to return true when both wanted and actual are null");
    }

    @Test
    void testMatchesNullWantedAndNonNullActual() {
        Same matcher = new Same(null);
        
        assertFalse(matcher.matches(new Object()), "Expected the matcher to return false when wanted is null and actual is not");
    }

    @Test
    void testMatchesNonNullWantedAndNullActual() {
        Same matcher = new Same(new Object());
        
        assertFalse(matcher.matches(null), "Expected the matcher to return false when wanted is not null and actual is null");
    }

    @Test
    void testTypeWhenWantedIsNotNull() {
        String wanted = "test";
        Same matcher = new Same(wanted);
        
        assertEquals(String.class, matcher.type(), "Expected the type to be String.class when wanted is a String");
    }

    @Test
    void testTypeWhenWantedIsNull() {
        Same matcher = new Same(null);
        
        assertEquals(Void.class, matcher.type(), "Expected the type to be Void.class when wanted is null");
    }

    @Test
    void testToStringWithNonNullWanted() {
        String wanted = "test";
        Same matcher = new Same(wanted);
        
        assertEquals("same(\"test\")", matcher.toString(), "Expected toString to return 'same(\"test\")' when wanted is 'test'");
    }

    @Test
    void testToStringWithNullWanted() {
        Same matcher = new Same(null);
        
        assertEquals("same(null)", matcher.toString(), "Expected toString to return 'same(null)' when wanted is null");
    }
}