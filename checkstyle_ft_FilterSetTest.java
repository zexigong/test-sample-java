///////////////////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code and other text files for adherence to a set of rules.
// Copyright (C) 2001-2025 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
///////////////////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FilterSetTest {

    private static final AuditEvent AUDIT_EVENT = new AuditEvent(new Object());

    private FilterSet filterSet;

    @BeforeEach
    void setUp() {
        filterSet = new FilterSet();
    }

    @Test
    void testClear() {
        filterSet.clear();
        final Set<Filter> filters = filterSet.getFilters();
        assertTrue(filters.isEmpty(), "Filter set should be empty");
    }

    @Test
    void testAcceptClear() {
        filterSet.clear();
        assertTrue(filterSet.accept(AUDIT_EVENT), "Filter set should accept audit event");
    }

    @Test
    void testAcceptFalse() {
        filterSet.addFilter(event -> false);
        assertFalse(filterSet.accept(AUDIT_EVENT), "Filter set should not accept audit event");
    }

    @Test
    void testAcceptTrue() {
        filterSet.addFilter(event -> true);
        assertTrue(filterSet.accept(AUDIT_EVENT), "Filter set should accept audit event");
    }

    @Test
    void testRemove() {
        final Filter removeFilter = event -> true;
        filterSet.addFilter(event -> false);
        filterSet.addFilter(removeFilter);
        filterSet.removeFilter(removeFilter);
        assertFalse(filterSet.accept(AUDIT_EVENT), "Filter set should not accept audit event");
    }

    @Test
    void testToString() {
        filterSet.clear();
        assertTrue(filterSet.toString().contains("[]"),
                "Filter set should accept audit event");
    }

    @Test
    void testGetFilters() {
        final Filter removeFilter = event -> true;
        filterSet.addFilter(event -> false);
        filterSet.addFilter(removeFilter);
        filterSet.removeFilter(removeFilter);
        assertFalse(filterSet.getFilters().contains(removeFilter),
                "Filter set should not accept audit event");
    }

}