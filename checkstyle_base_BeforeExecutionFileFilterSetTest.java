package com.puppycrawl.tools.checkstyle.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.HashSet;

class BeforeExecutionFileFilterSetTest {

    private BeforeExecutionFileFilterSet filterSet;

    @BeforeEach
    void setUp() {
        filterSet = new BeforeExecutionFileFilterSet();
    }

    @Test
    void testAddBeforeExecutionFileFilter() {
        BeforeExecutionFileFilter filter = uri -> true;
        filterSet.addBeforeExecutionFileFilter(filter);
        Set<BeforeExecutionFileFilter> filters = filterSet.getBeforeExecutionFileFilters();
        assertTrue(filters.contains(filter), "Filter should be added to the set");
    }

    @Test
    void testRemoveBeforeExecutionFileFilter() {
        BeforeExecutionFileFilter filter = uri -> true;
        filterSet.addBeforeExecutionFileFilter(filter);
        filterSet.removeBeforeExecutionFileFilter(filter);
        Set<BeforeExecutionFileFilter> filters = filterSet.getBeforeExecutionFileFilters();
        assertFalse(filters.contains(filter), "Filter should be removed from the set");
    }

    @Test
    void testGetBeforeExecutionFileFilters() {
        BeforeExecutionFileFilter filter1 = uri -> true;
        BeforeExecutionFileFilter filter2 = uri -> false;
        filterSet.addBeforeExecutionFileFilter(filter1);
        filterSet.addBeforeExecutionFileFilter(filter2);
        Set<BeforeExecutionFileFilter> filters = filterSet.getBeforeExecutionFileFilters();
        assertEquals(2, filters.size(), "There should be two filters in the set");
        assertTrue(filters.contains(filter1) && filters.contains(filter2), "The set should contain both filters");
    }

    @Test
    void testToString() {
        BeforeExecutionFileFilter filter = uri -> true;
        filterSet.addBeforeExecutionFileFilter(filter);
        String expected = "[" + filter.toString() + "]";
        assertEquals(expected, filterSet.toString(), "toString should return the correct string representation");
    }

    @Test
    void testAcceptWithAllAcceptingFilters() {
        BeforeExecutionFileFilter filter1 = uri -> true;
        BeforeExecutionFileFilter filter2 = uri -> true;
        filterSet.addBeforeExecutionFileFilter(filter1);
        filterSet.addBeforeExecutionFileFilter(filter2);
        assertTrue(filterSet.accept("testUri"), "All filters accepting should result in accept returning true");
    }

    @Test
    void testAcceptWithOneRejectingFilter() {
        BeforeExecutionFileFilter filter1 = uri -> true;
        BeforeExecutionFileFilter filter2 = uri -> false;
        filterSet.addBeforeExecutionFileFilter(filter1);
        filterSet.addBeforeExecutionFileFilter(filter2);
        assertFalse(filterSet.accept("testUri"), "One rejecting filter should result in accept returning false");
    }

    @Test
    void testClear() {
        BeforeExecutionFileFilter filter = uri -> true;
        filterSet.addBeforeExecutionFileFilter(filter);
        filterSet.clear();
        Set<BeforeExecutionFileFilter> filters = filterSet.getBeforeExecutionFileFilters();
        assertTrue(filters.isEmpty(), "Filter set should be empty after clear");
    }
}