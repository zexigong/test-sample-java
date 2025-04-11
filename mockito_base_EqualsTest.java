package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.*;

public class EqualsTest {

    @Test
    public void testMatchesWhenEqual() {
        Equals matcher = new Equals("test");
        assertTrue(matcher.matches("test"));
    }

    @Test
    public void testMatchesWhenNotEqual() {
        Equals matcher = new Equals("test");
        assertFalse(matcher.matches("notTest"));
    }

    @Test
    public void testMatchesWithNullWanted() {
        Equals matcher = new Equals(null);
        assertTrue(matcher.matches(null));
    }

    @Test
    public void testMatchesWithNullActual() {
        Equals matcher = new Equals("test");
        assertFalse(matcher.matches(null));
    }

    @Test
    public void testTypeWithNonNullWanted() {
        Equals matcher = new Equals("test");
        assertEquals(String.class, matcher.type());
    }

    @Test
    public void testTypeWithNullWanted() {
        Equals matcher = new Equals(null);
        assertEquals(Void.class, matcher.type());
    }

    @Test
    public void testToString() {
        Equals matcher = new Equals("test");
        assertEquals("\"test\"", matcher.toString());
    }

    @Test
    public void testGetWanted() {
        Equals matcher = new Equals("test");
        assertEquals("test", matcher.getWanted());
    }

    @Test
    public void testEqualsWithSameWanted() {
        Equals matcher1 = new Equals("test");
        Equals matcher2 = new Equals("test");
        assertTrue(matcher1.equals(matcher2));
    }

    @Test
    public void testEqualsWithDifferentWanted() {
        Equals matcher1 = new Equals("test");
        Equals matcher2 = new Equals("notTest");
        assertFalse(matcher1.equals(matcher2));
    }

    @Test
    public void testEqualsWithNullWanted() {
        Equals matcher1 = new Equals(null);
        Equals matcher2 = new Equals(null);
        assertTrue(matcher1.equals(matcher2));
    }

    @Test
    public void testEqualsWithDifferentType() {
        Equals matcher = new Equals("test");
        assertFalse(matcher.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Equals matcher = new Equals("test");
        assertEquals(1, matcher.hashCode());
    }

    @Test
    public void testToStringWithType() {
        Equals matcher = new Equals("test");
        assertEquals("(String) \"test\"", matcher.toStringWithType("String"));
    }

    @Test
    public void testTypeMatchesWhenTypesAreSame() {
        Equals matcher = new Equals("test");
        assertTrue(matcher.typeMatches("anotherTest"));
    }

    @Test
    public void testTypeMatchesWhenTypesAreDifferent() {
        Equals matcher = new Equals("test");
        assertFalse(matcher.typeMatches(123));
    }

    @Test
    public void testTypeMatchesWithNullWanted() {
        Equals matcher = new Equals(null);
        assertFalse(matcher.typeMatches("test"));
    }
}