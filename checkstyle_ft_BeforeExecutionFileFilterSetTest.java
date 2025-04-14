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

import static com.puppycrawl.tools.checkstyle.internal.utils.TestUtil.assertExceptionAndMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

public class BeforeExecutionFileFilterSetTest {

    private BeforeExecutionFileFilterSet beforeExecutionFileFilterSet;

    @BeforeEach
    public void setUp() {
        beforeExecutionFileFilterSet = new BeforeExecutionFileFilterSet();
    }

    @Test
    public void testToString() {
        assertEquals("[]", beforeExecutionFileFilterSet.toString(), "Invalid toString");
    }

    @Test
    public void testAddBeforeExecutionFileFilter() {
        beforeExecutionFileFilterSet.addBeforeExecutionFileFilter(new BeforeExecutionFileFilter() {
            @Override
            public boolean accept(String uri) {
                return true;
            }

            @Override
            public String toString() {
                return "BeforeExecutionFileFilter";
            }
        });
        assertEquals("[BeforeExecutionFileFilter]", beforeExecutionFileFilterSet.toString(),
            "Invalid toString");
    }

    @Test
    public void testClear() {
        beforeExecutionFileFilterSet.addBeforeExecutionFileFilter(new BeforeExecutionFileFilter() {
            @Override
            public boolean accept(String uri) {
                return true;
            }

            @Override
            public String toString() {
                return "BeforeExecutionFileFilter";
            }
        });
        beforeExecutionFileFilterSet.clear();
        assertEquals("[]", beforeExecutionFileFilterSet.toString(), "Invalid toString");
    }

    @Test
    public void testAccept() {
        final BeforeExecutionFileFilter filter1 = new BeforeExecutionFileFilter() {
            @Override
            public boolean accept(String uri) {
                return true;
            }

            @Override
            public String toString() {
                return "BeforeExecutionFileFilter1";
            }
        };
        final BeforeExecutionFileFilter filter2 = new BeforeExecutionFileFilter() {
            @Override
            public boolean accept(String uri) {
                return true;
            }

            @Override
            public String toString() {
                return "BeforeExecutionFileFilter2";
            }
        };
        final BeforeExecutionFileFilter filter3 = new BeforeExecutionFileFilter() {
            @Override
            public boolean accept(String uri) {
                return false;
            }

            @Override
            public String toString() {
                return "BeforeExecutionFileFilter3";
            }
        };
        beforeExecutionFileFilterSet.addBeforeExecutionFileFilter(filter1);
        beforeExecutionFileFilterSet.addBeforeExecutionFileFilter(filter2);
        assertTrue(beforeExecutionFileFilterSet.accept(""), "Invalid accept() return value");
        beforeExecutionFileFilterSet.addBeforeExecutionFileFilter(filter3);
        assertFalse(beforeExecutionFileFilterSet.accept(""), "Invalid accept() return value");
    }

    @Test
    public void testGetBeforeExecutionFileFilters() {
        final BeforeExecutionFileFilter filter = new BeforeExecutionFileFilter() {
            @Override
            public boolean accept(String uri) {
                return true;
            }

            @Override
            public String toString() {
                return "BeforeExecutionFileFilter";
            }
        };
        beforeExecutionFileFilterSet.addBeforeExecutionFileFilter(filter);
        beforeExecutionFileFilterSet.removeBeforeExecutionFileFilter(filter);
        assertEquals(0, beforeExecutionFileFilterSet.getBeforeExecutionFileFilters().size(),
            "Invalid getBeforeExecutionFileFilters() return value");
    }

    @Test
    public void testGetBeforeExecutionFileFiltersUnmodifiable() {
        final ThrowingSupplier<?> expectedThrowingSupplier =
            beforeExecutionFileFilterSet.getBeforeExecutionFileFilters()::clear;
        assertExceptionAndMessage(expectedThrowingSupplier, UnsupportedOperationException.class,
            null);
    }

}