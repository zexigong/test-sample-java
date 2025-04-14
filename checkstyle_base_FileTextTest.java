package com.puppycrawl.tools.checkstyle.api;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FileTextTest {

    private static final String TEST_FILE_PATH = "src/test/resources/com/puppycrawl/tools/checkstyle/api/FileTextTest.java";

    @Test
    public void testFileTextFromFileAndCharset() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        
        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
    }

    @Test
    public void testFileTextFromFileAndLines() {
        File file = new File(TEST_FILE_PATH);
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        
        FileText fileText = new FileText(file, lines);

        assertNotNull(fileText);
        assertEquals(file, fileText.getFile());
        assertEquals(null, fileText.getCharset());
        assertEquals("line1\nline2\nline3\n", fileText.getFullText().toString());
        assertArrayEquals(lines.toArray(), fileText.toLinesArray());
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = new File(TEST_FILE_PATH);
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        
        FileText originalFileText = new FileText(file, lines);
        FileText copyFileText = new FileText(originalFileText);

        assertNotNull(copyFileText);
        assertEquals(originalFileText.getFile(), copyFileText.getFile());
        assertEquals(originalFileText.getCharset(), copyFileText.getCharset());
        assertEquals(originalFileText.getFullText().toString(), copyFileText.getFullText().toString());
        assertArrayEquals(originalFileText.toLinesArray(), copyFileText.toLinesArray());
    }

    @Test
    public void testGetLine() {
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = new FileText(null, lines);

        assertEquals("line1", fileText.get(0));
        assertEquals("line2", fileText.get(1));
        assertEquals("line3", fileText.get(2));
    }

    @Test
    public void testLineColumn() throws IOException {
        File file = new File(TEST_FILE_PATH);
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());

        String fullText = fileText.getFullText().toString();
        int pos = fullText.indexOf("import");
        LineColumn lineColumn = fileText.lineColumn(pos);

        assertEquals(1, lineColumn.getLine());
        assertEquals(0, lineColumn.getColumn());
    }

    @Test
    public void testUnsupportedCharsetException() {
        File file = new File(TEST_FILE_PATH);
        assertThrows(IllegalStateException.class, () -> new FileText(file, "invalid-charset"));
    }

    @Test
    public void testFileNotFoundException() {
        File file = new File("nonexistent.file");
        assertThrows(FileNotFoundException.class, () -> new FileText(file, StandardCharsets.UTF_8.name()));
    }

    @Test
    public void testSize() {
        List<String> lines = Arrays.asList("line1", "line2", "line3");
        FileText fileText = new FileText(null, lines);

        assertEquals(3, fileText.size());
    }
}