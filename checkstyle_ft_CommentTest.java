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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void testComment() {
        final String[] text = {"line1", "line2"};
        final Comment comment = new Comment(text, 5, 8, 2);
        assertArrayEquals(text, comment.getText(),
                "Comment has the wrong text");
        assertEquals(5, comment.getStartLineNo(),
                "Comment has the wrong start line number");
        assertEquals(8, comment.getEndLineNo(),
                "Comment has the wrong end line number");
        assertEquals(5, comment.getStartColNo(),
                "Comment has the wrong start column number");
        assertEquals(2, comment.getEndColNo(),
                "Comment has the wrong end column number");
    }

    @Test
    void testCommentIntersects() {
        final String[] text = {"line1", "line2"};
        final Comment comment = new Comment(text, 5, 8, 2);

        // test intersecting comment block
        assertTrue(comment.intersects(5, 5, 8, 2),
                "Comments should be intersecting");

        // test intersecting comment block
        assertTrue(comment.intersects(5, 5, 5, 5),
                "Comments should be intersecting");

        // test intersecting comment block
        assertTrue(comment.intersects(5, 5, 5, 10),
                "Comments should be intersecting");

        // test intersecting comment block
        assertTrue(comment.intersects(5, 5, 6, 10),
                "Comments should be intersecting");

        // test intersecting comment block
        assertTrue(comment.intersects(8, 1, 8, 2),
                "Comments should be intersecting");

        // test intersecting comment block
        assertTrue(comment.intersects(8, 1, 8, 3),
                "Comments should be intersecting");

        // test non-intersecting comment block
        assertFalse(comment.intersects(1, 5, 4, 10),
                "Comments should not be intersecting");

        // test non-intersecting comment block
        assertFalse(comment.intersects(9, 1, 12, 10),
                "Comments should not be intersecting");
    }

    @Test
    void testCommentIntersectsMultiLine() {
        final String[] text = {"line1", "line2", "line3"};
        final Comment comment = new Comment(text, 1, 3, 2);

        // test intersecting comment block
        assertTrue(comment.intersects(2, 1, 2, 1),
                "Comments should be intersecting");
    }

    @Test
    void testCommentIntersectsMultiLine2() {
        final String[] text = {"line1", "line2", "line3"};
        final Comment comment = new Comment(text, 1, 3, 2);

        // test intersecting comment block
        assertTrue(comment.intersects(1, 0, 3, 2),
                "Comments should be intersecting");
    }

    @Test
    void testCommentIntersectsMultiLine3() {
        final String[] text = {"line1", "line2", "line3"};
        final Comment comment = new Comment(text, 1, 3, 2);

        // test intersecting comment block
        assertTrue(comment.intersects(2, 0, 2, 2),
                "Comments should be intersecting");
    }

    @Test
    void testCommentIntersectsMultiLine4() {
        final String[] text = {"line1", "line2", "line3"};
        final Comment comment = new Comment(text, 1, 3, 2);

        // test non-intersecting comment block
        assertFalse(comment.intersects(4, 0, 5, 0),
                "Comments should not be intersecting");
    }

    @Test
    void testCommentIntersectsMultiLine5() {
        final String[] text = {"line1", "line2", "line3"};
        final Comment comment = new Comment(text, 1, 3, 2);

        // test non-intersecting comment block
        assertFalse(comment.intersects(0, 0, 0, 1),
                "Comments should not be intersecting");
    }

}