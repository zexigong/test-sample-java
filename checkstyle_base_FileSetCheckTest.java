package com.puppycrawl.tools.checkstyle.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FileSetCheckTest {

    private FileSetCheck fileSetCheck;
    private MessageDispatcher mockDispatcher;

    @BeforeEach
    void setUp() {
        fileSetCheck = mock(FileSetCheck.class);
        mockDispatcher = mock(MessageDispatcher.class);
    }

    @Test
    void testSetMessageDispatcher() {
        assertDoesNotThrow(() -> fileSetCheck.setMessageDispatcher(mockDispatcher));
        verify(fileSetCheck).setMessageDispatcher(mockDispatcher);
    }

    @Test
    void testInit() {
        assertDoesNotThrow(() -> fileSetCheck.init());
        verify(fileSetCheck).init();
    }

    @Test
    void testDestroy() {
        assertDoesNotThrow(() -> fileSetCheck.destroy());
        verify(fileSetCheck).destroy();
    }

    @Test
    void testBeginProcessing() {
        String charset = "UTF-8";
        assertDoesNotThrow(() -> fileSetCheck.beginProcessing(charset));
        verify(fileSetCheck).beginProcessing(charset);
    }

    @Test
    void testProcess() throws CheckstyleException {
        File file = new File("testFile.java");
        FileText fileText = new FileText(file, "public class Test {}".toCharArray());
        SortedSet<Violation> expectedViolations = new TreeSet<>();

        Mockito.when(fileSetCheck.process(file, fileText)).thenReturn(expectedViolations);

        SortedSet<Violation> violations = fileSetCheck.process(file, fileText);

        assertEquals(expectedViolations, violations);
        verify(fileSetCheck).process(file, fileText);
    }

    @Test
    void testProcessThrowsCheckstyleException() throws CheckstyleException {
        File file = new File("testFile.java");
        FileText fileText = new FileText(file, "public class Test {}".toCharArray());

        Mockito.when(fileSetCheck.process(file, fileText)).thenThrow(CheckstyleException.class);

        assertThrows(CheckstyleException.class, () -> fileSetCheck.process(file, fileText));
        verify(fileSetCheck).process(file, fileText);
    }

    @Test
    void testFinishProcessing() {
        assertDoesNotThrow(() -> fileSetCheck.finishProcessing());
        verify(fileSetCheck).finishProcessing();
    }
}