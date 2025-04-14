package com.puppycrawl.tools.checkstyle.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilterSetTest {

    private FilterSet filterSet;
    private AuditEvent mockEvent;

    @BeforeEach
    void setUp() {
        filterSet = new FilterSet();
        mockEvent = mock(AuditEvent.class);
    }

    @Test
    void testAddFilter() {
        Filter mockFilter = mock(Filter.class);
        filterSet.addFilter(mockFilter);

        Set<Filter> filters = filterSet.getFilters();
        assertTrue(filters.contains(mockFilter), "Filter should be added to the set");
    }

    @Test
    void testRemoveFilter() {
        Filter mockFilter = mock(Filter.class);
        filterSet.addFilter(mockFilter);
        filterSet.removeFilter(mockFilter);

        Set<Filter> filters = filterSet.getFilters();
        assertFalse(filters.contains(mockFilter), "Filter should be removed from the set");
    }

    @Test
    void testGetFilters() {
        Filter mockFilter1 = mock(Filter.class);
        Filter mockFilter2 = mock(Filter.class);

        filterSet.addFilter(mockFilter1);
        filterSet.addFilter(mockFilter2);

        Set<Filter> filters = filterSet.getFilters();
        assertEquals(2, filters.size(), "Filter set should contain 2 filters");
        assertTrue(filters.contains(mockFilter1), "Filter set should contain mockFilter1");
        assertTrue(filters.contains(mockFilter2), "Filter set should contain mockFilter2");
    }

    @Test
    void testAcceptAllFiltersAccept() {
        Filter mockFilter1 = mock(Filter.class);
        Filter mockFilter2 = mock(Filter.class);

        when(mockFilter1.accept(mockEvent)).thenReturn(true);
        when(mockFilter2.accept(mockEvent)).thenReturn(true);

        filterSet.addFilter(mockFilter1);
        filterSet.addFilter(mockFilter2);

        assertTrue(filterSet.accept(mockEvent), "Event should be accepted as all filters accept it");
    }

    @Test
    void testAcceptOneFilterRejects() {
        Filter mockFilter1 = mock(Filter.class);
        Filter mockFilter2 = mock(Filter.class);

        when(mockFilter1.accept(mockEvent)).thenReturn(true);
        when(mockFilter2.accept(mockEvent)).thenReturn(false);

        filterSet.addFilter(mockFilter1);
        filterSet.addFilter(mockFilter2);

        assertFalse(filterSet.accept(mockEvent), "Event should be rejected as one filter rejects it");
    }

    @Test
    void testAcceptNoFilters() {
        assertTrue(filterSet.accept(mockEvent), "Event should be accepted as there are no filters to reject it");
    }

    @Test
    void testClear() {
        Filter mockFilter = mock(Filter.class);
        filterSet.addFilter(mockFilter);

        filterSet.clear();
        Set<Filter> filters = filterSet.getFilters();
        assertTrue(filters.isEmpty(), "Filter set should be empty after clear");
    }

    @Test
    void testToString() {
        Filter mockFilter1 = mock(Filter.class);
        Filter mockFilter2 = mock(Filter.class);

        filterSet.addFilter(mockFilter1);
        filterSet.addFilter(mockFilter2);

        String filterSetString = filterSet.toString();
        assertNotNull(filterSetString, "toString should not return null");
        assertTrue(filterSetString.contains(mockFilter1.toString()), "toString should contain mockFilter1");
        assertTrue(filterSetString.contains(mockFilter2.toString()), "toString should contain mockFilter2");
    }
}