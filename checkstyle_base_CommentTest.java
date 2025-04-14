package com.puppycrawl.tools.checkstyle.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void testConstructorAndGetters() {
        String[] text = {"This is a comment", "Second line of comment"};
        int firstCol = 5;
        int lastLine = 10;
        int lastCol = 20;

        Comment comment = new Comment(text, firstCol, lastLine, lastCol);

        assertEquals(lastLine - text.length + 1, comment.getStartLineNo());
        assertEquals(lastLine, comment.getEndLineNo());
        assertEquals(firstCol, comment.getStartColNo());
        assertEquals(lastCol, comment.getEndColNo());
        assertEquals(text, comment.getText());
    }

    @Test
    void testIntersectsTrue() {
        String[] text = {"This is a comment"};
        Comment comment = new Comment(text, 5, 10, 20);

        assertTrue(comment.intersects(9, 15, 10, 25), "Expected intersection with overlapping range");
        assertTrue(comment.intersects(10, 19, 11, 5), "Expected intersection with overlapping range");
        assertTrue(comment.intersects(5, 5, 10, 20), "Expected intersection with exact range");
    }

    @Test
    void testIntersectsFalse() {
        String[] text = {"This is a comment"};
        Comment comment = new Comment(text, 5, 10, 20);

        assertFalse(comment.intersects(11, 5, 12, 5), "Expected no intersection with non-overlapping range");
        assertFalse(comment.intersects(1, 1, 4, 4), "Expected no intersection with non-overlapping range");
    }

    @Test
    void testToString() {
        String[] text = {"This is a comment", "Second line of comment"};
        Comment comment = new Comment(text, 5, 10, 20);

        String expected = "Comment[text=[This is a comment, Second line of comment], startLineNo=9, endLineNo=10, startColNo=5, endColNo=20]";
        assertEquals(expected, comment.toString());
    }
}