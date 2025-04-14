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
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;

public class FileSetCheckTest extends AbstractModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/puppycrawl/tools/checkstyle/api/filesetcheck";
    }

    @Test
    public void testInvalid() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(ModuleThatThrowsAnExceptionCheck.class);
        final String[] expected = {
            "0: " + getCheckMessage("general.exception",
                    "java.lang.ClassCastException: "
                            + "java.lang.String cannot be cast to java.lang.Integer"),
        };
        verify(createChecker(checkConfig), getPath("InputFileSetCheck.java"), expected);
    }

    @Test
    public void testInvalidWithException() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(ModuleThatThrowsAnExceptionCheck.class);

        final String message = getCheckMessage("general.exception",
                "java.lang.ClassCastException: "
                        + "java.lang.String cannot be cast to java.lang.Integer");
        final Exception exception = assertThrows(CheckstyleException.class,
                () -> verify(createChecker(checkConfig), getPath("InputFileSetCheck.java"),
                        CommonUtils.EMPTY_STRING_ARRAY));
        assertWithMessage("Invalid message")
                .that(exception.getMessage())
                .isEqualTo(message);
    }

    public static final class ModuleThatThrowsAnExceptionCheck implements FileSetCheck {

        @Override
        public void setMessageDispatcher(MessageDispatcher dispatcher) {
            // no code
        }

        @Override
        public void configure(Configuration configuration) {
            // no code
        }

        @Override
        public void init() {
            // no code
        }

        @Override
        public void destroy() {
            // no code
        }

        @Override
        public void contextualize(Context context) {
            // no code
        }

        @Override
        public void beginProcessing(String charset) {
            // no code
        }

        @Override
        public SortedSet<Violation> process(File file, FileText fileText)
                throws CheckstyleException {
            throw new ClassCastException("java.lang.String cannot be cast to java.lang.Integer");
        }

        @Override
        public void finishProcessing() {
            // no code
        }

    }
}