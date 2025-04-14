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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AbstractViolationReporterTest {

    @Test
    void testSeverity() {
        final AbstractViolationReporter reporter = new AbstractViolationReporterImpl();
        assertEquals(SeverityLevel.ERROR, reporter.getSeverityLevel(),
                "Default severity value is invalid");
        final String severity = "warning";
        reporter.setSeverity(severity);
        assertEquals(severity, reporter.getSeverity(), "Invalid severity value");
        assertEquals(SeverityLevel.WARNING, reporter.getSeverityLevel(),
                "Invalid severity value");
    }

    @Test
    void testId() {
        final AbstractViolationReporter reporter = new AbstractViolationReporterImpl();
        assertEquals(null, reporter.getId(), "Default id value is invalid");
        final String id = "MyId";
        reporter.setId(id);
        assertEquals(id, reporter.getId(), "Invalid id value");
    }

    private static final class AbstractViolationReporterImpl
        extends AbstractViolationReporter {

        @Override
        public void log(int line, String key, Object... args) {
            // No code
        }

        @Override
        public void log(int line, int col, String key, Object... args) {
            // No code
        }

    }
}