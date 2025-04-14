package com.puppycrawl.tools.checkstyle.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractViolationReporterTest {

    private TestViolationReporter reporter;

    @BeforeEach
    public void setUp() {
        reporter = new TestViolationReporter();
    }

    @Test
    public void testDefaultSeverityLevel() {
        assertEquals(SeverityLevel.ERROR, reporter.getSeverityLevel(),
                "Default severity level should be ERROR");
    }

    @Test
    public void testSetSeverity() {
        reporter.setSeverity("WARNING");
        assertEquals(SeverityLevel.WARNING, reporter.getSeverityLevel(),
                "Severity level should be set to WARNING");
    }

    @Test
    public void testGetSeverity() {
        reporter.setSeverity("INFO");
        assertEquals("INFO", reporter.getSeverity(),
                "Severity name should be INFO");
    }

    @Test
    public void testSetAndGetId() {
        assertNull(reporter.getId(), "Default ID should be null");

        reporter.setId("testId");
        assertEquals("testId", reporter.getId(), "ID should be set to testId");
    }

    @Test
    public void testGetMessageBundle() {
        String expectedBundleName = "com.puppycrawl.tools.checkstyle.api.messages";
        assertEquals(expectedBundleName, reporter.getMessageBundle(),
                "Message bundle name should match the expected value");
    }

    // Concrete implementation of AbstractViolationReporter for testing
    private static class TestViolationReporter extends AbstractViolationReporter {
        @Override
        public void log(int line, String key, Object... args) {
            // No-op implementation for testing
        }

        @Override
        public void log(int line, int col, String key, Object... args) {
            // No-op implementation for testing
        }
    }
}