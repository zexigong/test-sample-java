package org.mockito.internal.reporting;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PluralizerTest {

    @Test
    public void testPluralize_single() {
        assertEquals("1 time", Pluralizer.pluralize(1));
    }

    @Test
    public void testPluralize_multiple() {
        assertEquals("2 times", Pluralizer.pluralize(2));
        assertEquals("10 times", Pluralizer.pluralize(10));
        assertEquals("0 times", Pluralizer.pluralize(0));
    }

    @Test
    public void testWereExactlyXInteractions_single() {
        assertEquals("was exactly 1 interaction", Pluralizer.were_exactly_x_interactions(1));
    }

    @Test
    public void testWereExactlyXInteractions_multiple() {
        assertEquals("were exactly 2 interactions", Pluralizer.were_exactly_x_interactions(2));
        assertEquals("were exactly 10 interactions", Pluralizer.were_exactly_x_interactions(10));
        assertEquals("were exactly 0 interactions", Pluralizer.were_exactly_x_interactions(0));
    }
}