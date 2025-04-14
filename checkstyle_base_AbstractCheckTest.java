package com.puppycrawl.tools.checkstyle.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.SortedSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class AbstractCheckTest {

    private AbstractCheck abstractCheck;

    @BeforeEach
    public void setUp() {
        abstractCheck = new AbstractCheck() {
            @Override
            public int[] getDefaultTokens() {
                return new int[] {1, 2, 3};
            }

            @Override
            public int[] getAcceptableTokens() {
                return new int[] {1, 2, 3, 4};
            }

            @Override
            public int[] getRequiredTokens() {
                return new int[] {1};
            }
        };
    }

    @Test
    public void testGetDefaultTokens() {
        int[] expectedTokens = {1, 2, 3};
        assertEquals(expectedTokens.length, abstractCheck.getDefaultTokens().length);
    }

    @Test
    public void testGetAcceptableTokens() {
        int[] expectedTokens = {1, 2, 3, 4};
        assertEquals(expectedTokens.length, abstractCheck.getAcceptableTokens().length);
    }

    @Test
    public void testGetRequiredTokens() {
        int[] expectedTokens = {1};
        assertEquals(expectedTokens.length, abstractCheck.getRequiredTokens().length);
    }

    @Test
    public void testIsCommentNodesRequired() {
        assertFalse(abstractCheck.isCommentNodesRequired());
    }

    @Test
    public void testSetAndGetTokens() {
        String[] tokens = {"TOKEN1", "TOKEN2"};
        abstractCheck.setTokens(tokens);
        Set<String> tokenNames = abstractCheck.getTokenNames();
        assertNotNull(tokenNames);
        assertEquals(2, tokenNames.size());
        assertTrue(tokenNames.contains("TOKEN1"));
        assertTrue(tokenNames.contains("TOKEN2"));
    }

    @Test
    public void testGetViolations() {
        SortedSet<Violation> violations = abstractCheck.getViolations();
        assertNotNull(violations);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testClearViolations() {
        abstractCheck.log(1, "testKey", "arg1");
        SortedSet<Violation> violations = abstractCheck.getViolations();
        assertFalse(violations.isEmpty());
        abstractCheck.clearViolations();
        assertTrue(abstractCheck.getViolations().isEmpty());
    }

    @Test
    public void testSetAndGetTabWidth() {
        abstractCheck.setTabWidth(4);
        assertEquals(4, abstractCheck.getTabWidth());
    }

    @Test
    public void testLogWithLineAndKey() {
        abstractCheck.log(1, "testKey", "arg1");
        SortedSet<Violation> violations = abstractCheck.getViolations();
        assertEquals(1, violations.size());
    }

    @Test
    public void testLogWithLineColumnAndKey() {
        abstractCheck.setTabWidth(CommonUtil.DEFAULT_TAB_WIDTH);
        abstractCheck.log(1, 0, "testKey", "arg1");
        SortedSet<Violation> violations = abstractCheck.getViolations();
        assertEquals(1, violations.size());
    }

    @Test
    public void testLogWithASTAndKey() {
        DetailAST ast = new DetailAST();
        ast.setLineNo(1);
        ast.setColumnNo(0);
        abstractCheck.setTabWidth(CommonUtil.DEFAULT_TAB_WIDTH);
        abstractCheck.log(ast, "testKey", "arg1");
        SortedSet<Violation> violations = abstractCheck.getViolations();
        assertEquals(1, violations.size());
    }

    @Test
    public void testGetLines() {
        FileContents contents = new FileContents(new FileText("file", new String[] {"line1", "line2"}));
        abstractCheck.setFileContents(contents);
        String[] lines = abstractCheck.getLines();
        assertEquals(2, lines.length);
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
    }

    @Test
    public void testGetLine() {
        FileContents contents = new FileContents(new FileText("file", new String[] {"line1", "line2"}));
        abstractCheck.setFileContents(contents);
        assertEquals("line1", abstractCheck.getLine(0));
        assertEquals("line2", abstractCheck.getLine(1));
    }

    @Test
    public void testGetFilePath() {
        FileContents contents = new FileContents(new FileText("file", new String[] {"line1", "line2"}));
        abstractCheck.setFileContents(contents);
        assertEquals("file", abstractCheck.getFilePath());
    }

    @Test
    public void testGetLineCodePoints() {
        FileContents contents = new FileContents(new FileText("file", new String[] {"line1", "line2"}));
        abstractCheck.setFileContents(contents);
        int[] codePoints = abstractCheck.getLineCodePoints(0);
        assertNotNull(codePoints);
        assertEquals(5, codePoints.length);
    }

    @Test
    public void testDestroy() {
        abstractCheck.destroy();
        assertThrows(NullPointerException.class, () -> abstractCheck.getFileContents());
    }
}