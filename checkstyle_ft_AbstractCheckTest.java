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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AbstractCheckTest {

    @Test
    public void testGetDefaultTokens() {
        final AbstractCheck check = new AbstractCheck() {
            @Override
            public int[] getDefaultTokens() {
                return new int[] {1, 2};
            }

            @Override
            public int[] getAcceptableTokens() {
                return null;
            }

            @Override
            public int[] getRequiredTokens() {
                return null;
            }
        };

        assertArrayEquals(new int[] {1, 2}, check.getDefaultTokens());
    }

    @Test
    public void testGetRequiredTokens() {
        final AbstractCheck check = new AbstractCheck() {
            @Override
            public int[] getDefaultTokens() {
                return null;
            }

            @Override
            public int[] getAcceptableTokens() {
                return null;
            }

            @Override
            public int[] getRequiredTokens() {
                return new int[] {1, 2};
            }
        };

        assertArrayEquals(new int[] {1, 2}, check.getRequiredTokens());
    }

    @Test
    public void testGetAcceptableTokens() {
        final AbstractCheck check = new AbstractCheck() {
            @Override
            public int[] getDefaultTokens() {
                return null;
            }

            @Override
            public int[] getAcceptableTokens() {
                return new int[] {1, 2};
            }

            @Override
            public int[] getRequiredTokens() {
                return null;
            }
        };

        assertArrayEquals(new int[] {1, 2}, check.getAcceptableTokens());
    }

    @Test
    public void testGetFilePath() {
        final AbstractCheck check = new AbstractCheck() {
            @Override
            public int[] getDefaultTokens() {
                return null;
            }

            @Override
            public int[] getAcceptableTokens() {
                return null;
            }

            @Override
            public int[] getRequiredTokens() {
                return null;
            }
        };

        final FileContents contents = new FileContents(null, "SomeFile.java", null);
        check.setFileContents(contents);

        assertEquals("SomeFile.java", check.getFilePath());
    }

    @Test
    public void testGetLine() {
        final AbstractCheck check = new AbstractCheck() {
            @Override
            public int[] getDefaultTokens() {
                return null;
            }

            @Override
            public int[] getAcceptableTokens() {
                return null;
            }

            @Override
            public int[] getRequiredTokens() {
                return null;
            }
        };

        final FileContents contents = new FileContents(null, null, new String[] {"line1", "line2"});
        check.setFileContents(contents);

        assertEquals("line1", check.getLine(0));
        assertEquals("line2", check.getLine(1));
    }

    @Test
    public void testGetLines() {
        final AbstractCheck check = new AbstractCheck() {
            @Override
            public int[] getDefaultTokens() {
                return null;
            }

            @Override
            public int[] getAcceptableTokens() {
                return null;
            }

            @Override
            public int[] getRequiredTokens() {
                return null;
            }
        };

        final FileContents contents = new FileContents(null, null, new String[] {"line1", "line2"});
        check.setFileContents(contents);

        assertArrayEquals(new String[] {"line1", "line2"}, check.getLines());
    }

    @Test
    public void testTokens() {
        final AbstractCheck check = new AbstractCheck() {
            @Override
            public int[] getDefaultTokens() {
                return null;
            }

            @Override
            public int[] getAcceptableTokens() {
                return null;
            }

            @Override
            public int[] getRequiredTokens() {
                return null;
            }
        };

        check.setTokens("1", "2");

        assertEquals(2, check.getTokenNames().size());
        assertTrue(check.getTokenNames().contains("1"));
        assertTrue(check.getTokenNames().contains("2"));
    }

    @Test
    public void testGetLineCodePoints() {
        final AbstractCheck check = new AbstractCheck() {
            @Override
            public int[] getDefaultTokens() {
                return null;
            }

            @Override
            public int[] getAcceptableTokens() {
                return null;
            }

            @Override
            public int[] getRequiredTokens() {
                return null;
            }
        };

        final FileContents contents = new FileContents(null, null, new String[] {"line1", "line2"});
        check.setFileContents(contents);

        assertArrayEquals("line1".codePoints().toArray(), check.getLineCodePoints(0));
        assertArrayEquals("line2".codePoints().toArray(), check.getLineCodePoints(1));
    }

}