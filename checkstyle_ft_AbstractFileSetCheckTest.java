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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractFileSetCheckTest {

    private static final String[] FILE_EXTENSIONS = {".java", ".xml"};
    private static final int TAB_WIDTH = 8;

    private final AbstractFileSetCheck check = new AbstractFileSetCheck() {
        @Override
        protected void processFiltered(File file, FileText fileText) {
            // no code
        }
    };

    @BeforeEach
    public void setUp() {
        check.setFileExtensions(FILE_EXTENSIONS);
        check.setTabWidth(TAB_WIDTH);
    }

    @Test
    public void testGetFileExtensions() {
        assertArrayEquals(FILE_EXTENSIONS, check.getFileExtensions(),
                "Invalid file extensions");
    }

    @Test
    public void testGetTabWidth() {
        assertEquals(TAB_WIDTH, check.getTabWidth(),
                "Invalid tab width");
    }

    @Test
    public void testSetFileExtensionsWithNullArray() {
        assertThrows(IllegalArgumentException.class, () -> check.setFileExtensions((String[]) null));
    }

    @Test
    public void testAddViolations() {
        final SortedSet<Violation> violations = new TreeSet<>();
        violations.add(new Violation(1, 1, "message",
                new Object[0], SeverityLevel.WARNING, "id", getClass()));
        check.addViolations(violations);
        assertEquals(1, check.getViolations().size(), "Invalid number of violations");
        check.addViolations(violations);
        assertEquals(1, check.getViolations().size(), "Invalid number of violations");
    }
}