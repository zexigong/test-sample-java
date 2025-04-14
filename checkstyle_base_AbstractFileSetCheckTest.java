package com.puppycrawl.tools.checkstyle.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

class AbstractFileSetCheckTest {

    private AbstractFileSetCheck check;
    private File file;
    private FileText fileText;

    @BeforeEach
    void setUp() {
        check = new AbstractFileSetCheckImpl();
        file = new File("Test.java");
        fileText = new FileText(file, "public class Test {}");
    }

    @Test
    void testSetFileExtensionsWithNull() {
        assertThrows(IllegalArgumentException.class, () -> check.setFileExtensions((String[]) null));
    }

    @Test
    void testSetFileExtensions() {
        check.setFileExtensions("java", ".txt");
        String[] expected = {".java", ".txt"};
        assertEquals(expected, check.getFileExtensions());
    }

    @Test
    void testSetAndGetTabWidth() {
        check.setTabWidth(4);
        assertEquals(4, check.getTabWidth());
    }

    @Test
    void testProcessFiltered() throws CheckstyleException {
        check.setFileExtensions("java");
        SortedSet<Violation> violations = check.process(file, fileText);
        assertEquals(1, violations.size());
    }

    @Test
    void testAddViolations() {
        SortedSet<Violation> violations = new TreeSet<>();
        violations.add(new Violation(1, "messageBundle", "key"));
        check.addViolations(violations);
        assertEquals(violations, check.getViolations());
    }

    @Test
    void testFireErrors() {
        MessageDispatcher mockDispatcher = mock(MessageDispatcher.class);
        check.setMessageDispatcher(mockDispatcher);

        check.log(1, "key");
        check.fireErrors("Test.java");
        assertEquals(0, check.getViolations().size());
    }

    @Test
    void testFileExtensionsMatches() {
        check.setFileExtensions("java");
        assertEquals(true, CommonUtil.matchesFileExtension(file, check.getFileExtensions()));
    }

    private static class AbstractFileSetCheckImpl extends AbstractFileSetCheck {

        @Override
        protected void processFiltered(File file, FileText fileText) throws CheckstyleException {
            log(1, "key");
        }
    }
}