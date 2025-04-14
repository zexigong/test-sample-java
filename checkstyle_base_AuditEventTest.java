package com.puppycrawl.tools.checkstyle.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditEventTest {

    private static final Object TEST_SOURCE = new Object();
    private static final String TEST_FILENAME = "TestFile.java";

    private Violation mockViolation;

    @BeforeEach
    void setUp() {
        mockViolation = mock(Violation.class);
        when(mockViolation.getLineNo()).thenReturn(10);
        when(mockViolation.getColumnNo()).thenReturn(5);
        when(mockViolation.getViolation()).thenReturn("Test violation message");
        when(mockViolation.getSeverityLevel()).thenReturn(SeverityLevel.WARNING);
        when(mockViolation.getModuleId()).thenReturn("TestModule");
        when(mockViolation.getSourceName()).thenReturn("TestSourceName");
    }

    @Test
    void testConstructorWithSourceOnly() {
        AuditEvent event = new AuditEvent(TEST_SOURCE);
        assertEquals(TEST_SOURCE, event.getSource());
        assertNull(event.getFileName());
        assertNull(event.getViolation());
    }

    @Test
    void testConstructorWithSourceAndFileName() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME);
        assertEquals(TEST_SOURCE, event.getSource());
        assertEquals(TEST_FILENAME, event.getFileName());
        assertNull(event.getViolation());
    }

    @Test
    void testConstructorWithSourceFileNameAndViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME, mockViolation);
        assertEquals(TEST_SOURCE, event.getSource());
        assertEquals(TEST_FILENAME, event.getFileName());
        assertEquals(mockViolation, event.getViolation());
    }

    @Test
    void testGetLineWithViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME, mockViolation);
        assertEquals(10, event.getLine());
    }

    @Test
    void testGetLineWithoutViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME);
        assertThrows(NullPointerException.class, event::getLine);
    }

    @Test
    void testGetMessageWithViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME, mockViolation);
        assertEquals("Test violation message", event.getMessage());
    }

    @Test
    void testGetMessageWithoutViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME);
        assertThrows(NullPointerException.class, event::getMessage);
    }

    @Test
    void testGetColumnWithViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME, mockViolation);
        assertEquals(5, event.getColumn());
    }

    @Test
    void testGetColumnWithoutViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME);
        assertThrows(NullPointerException.class, event::getColumn);
    }

    @Test
    void testGetSeverityLevelWithViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME, mockViolation);
        assertEquals(SeverityLevel.WARNING, event.getSeverityLevel());
    }

    @Test
    void testGetSeverityLevelWithoutViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME);
        assertEquals(SeverityLevel.INFO, event.getSeverityLevel());
    }

    @Test
    void testGetModuleIdWithViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME, mockViolation);
        assertEquals("TestModule", event.getModuleId());
    }

    @Test
    void testGetModuleIdWithoutViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME);
        assertThrows(NullPointerException.class, event::getModuleId);
    }

    @Test
    void testGetSourceNameWithViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME, mockViolation);
        assertEquals("TestSourceName", event.getSourceName());
    }

    @Test
    void testGetSourceNameWithoutViolation() {
        AuditEvent event = new AuditEvent(TEST_SOURCE, TEST_FILENAME);
        assertThrows(NullPointerException.class, event::getSourceName);
    }

    @Test
    void testConstructorThrowsExceptionWhenSourceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new AuditEvent(null));
    }
}