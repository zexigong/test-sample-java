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

import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.BaseCheckTestSupport;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

class FileTextTest extends BaseCheckTestSupport {

    @Test
    void testCommonConstructor() throws IOException {
        final File file = new File(getPath("InputFileText.java"));
        final FileText text = new FileText(file, "UTF-8");
        assertWithMessage("Invalid file name")
            .that(text.getFile())
            .isEqualTo(file);
        assertWithMessage("Invalid charset")
            .that(text.getCharset())
            .isEqualTo(CommonUtil.UTF_8_CHARSET);
        assertWithMessage("Invalid line count")
            .that(text.size())
            .isEqualTo(3);
        assertWithMessage("Invalid line 0")
            .that(text.get(0))
            .isEqualTo("package test;");
        assertWithMessage("Invalid line 1")
            .that(text.get(1))
            .isEqualTo("");
        assertWithMessage("Invalid line 2")
            .that(text.get(2))
            .isEqualTo("interface InputFileText {}");
    }

    @Test
    void testCompatibilityConstructor() {
        final File file = new File("Test.java");
        final List<String> lines = new ArrayList<>();
        lines.add("package test;");
        lines.add("");
        lines.add("interface Test {}");

        final FileText text = new FileText(file, lines);
        assertWithMessage("Invalid file name")
            .that(text.getFile())
            .isEqualTo(file);
        assertWithMessage("Invalid charset")
            .that(text.getCharset())
            .isNull();
        assertWithMessage("Invalid line count")
            .that(text.size())
            .isEqualTo(3);
        assertWithMessage("Invalid line 0")
            .that(text.get(0))
            .isEqualTo("package test;");
        assertWithMessage("Invalid line 1")
            .that(text.get(1))
            .isEmpty();
        assertWithMessage("Invalid line 2")
            .that(text.get(2))
            .isEqualTo("interface Test {}");
    }

    @Test
    void testLineColumn() throws IOException {
        final FileText text = new FileText(new File(getPath("InputFileText.java")), "UTF-8");
        assertWithMessage("Invalid line/column for character 0")
            .that(text.lineColumn(0))
            .isEqualTo(new LineColumn(1, 0));
        assertWithMessage("Invalid line/column for character 8")
            .that(text.lineColumn(8))
            .isEqualTo(new LineColumn(1, 8));
        assertWithMessage("Invalid line/column for character 15")
            .that(text.lineColumn(15))
            .isEqualTo(new LineColumn(1, 15));
        assertWithMessage("Invalid line/column for character 16")
            .that(text.lineColumn(16))
            .isEqualTo(new LineColumn(2, 0));
        assertWithMessage("Invalid line/column for character 17")
            .that(text.lineColumn(17))
            .isEqualTo(new LineColumn(3, 0));
        assertWithMessage("Invalid line/column for character 25")
            .that(text.lineColumn(25))
            .isEqualTo(new LineColumn(3, 8));
    }

    @Test
    void testUnsupportedCharset() {
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> new FileText(new File(getPath("InputFileText.java")), "unsupported"));
        assertWithMessage("Invalid exception message")
            .that(exception.getMessage())
            .isEqualTo("Unsupported charset: unsupported");
    }

}