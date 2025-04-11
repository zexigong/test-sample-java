package org.mockito.internal.matchers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InstanceOfTest {

    @Test
    void testMatchesWithSameClass() {
        InstanceOf matcher = new InstanceOf(String.class);
        assertTrue(matcher.matches("test"));
    }

    @Test
    void testMatchesWithSubclass() {
        InstanceOf matcher = new InstanceOf(Number.class);
        assertTrue(matcher.matches(123));
    }

    @Test
    void testMatchesWithPrimitiveWrapper() {
        InstanceOf matcher = new InstanceOf(int.class);
        assertTrue(matcher.matches(123));
    }

    @Test
    void testDoesNotMatchWithDifferentClass() {
        InstanceOf matcher = new InstanceOf(String.class);
        assertFalse(matcher.matches(123));
    }

    @Test
    void testDoesNotMatchWithNull() {
        InstanceOf matcher = new InstanceOf(Object.class);
        assertFalse(matcher.matches(null));
    }

    @Test
    void testTypeMethod() {
        InstanceOf matcher = new InstanceOf(String.class);
        assertEquals(String.class, matcher.type());
    }

    @Test
    void testToStringMethod() {
        InstanceOf matcher = new InstanceOf(String.class);
        assertEquals("isA(java.lang.String)", matcher.toString());
    }

    @Test
    void testCustomDescription() {
        InstanceOf matcher = new InstanceOf(String.class, "Custom description");
        assertEquals("Custom description", matcher.toString());
    }
}