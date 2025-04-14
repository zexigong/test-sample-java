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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link AuditEvent}.
 */
public class AuditEventTest {

    @Test
    public void testCtor() {
        final AuditEvent evt = new AuditEvent(this);
        assertNull(evt.getFileName(), "File name is not null");
        assertEquals(this, evt.getSource(), "Invalid source");
    }

    @Test
    public void testCtorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> new AuditEvent(null));
    }

    @Test
    public void testCtorTwoParams() {
        final AuditEvent evt = new AuditEvent(this, "filename");
        assertEquals("filename", evt.getFileName(), "File name is not correct");
        assertEquals(this, evt.getSource(), "Invalid source");
    }

    @Test
    public void testCtorThreeParams() {
        final Violation violation = new Violation(0, 0,
            SeverityLevel.ERROR, "violation msg", null, null);
        final AuditEvent evt = new AuditEvent(this, "filename", violation);
        assertEquals("filename", evt.getFileName(), "File name is not correct");
        assertEquals(this, evt.getSource(), "Invalid source");
        assertEquals(violation, evt.getViolation(), "Violation is not correct");
    }
}