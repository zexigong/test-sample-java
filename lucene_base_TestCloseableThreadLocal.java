package org.apache.lucene.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCloseableThreadLocal {

    private CloseableThreadLocal<String> threadLocal;

    @Before
    public void setUp() {
        threadLocal = new CloseableThreadLocal<>();
    }

    @After
    public void tearDown() {
        threadLocal.close();
    }

    @Test
    public void testInitialValue() {
        assertNull("Initial value should be null", threadLocal.get());
    }

    @Test
    public void testSetAndGet() {
        String testValue = "test";
        threadLocal.set(testValue);
        assertEquals("The set value should be retrieved", testValue, threadLocal.get());
    }

    @Test
    public void testMultipleThreads() throws InterruptedException {
        String mainThreadValue = "mainThreadValue";
        String otherThreadValue = "otherThreadValue";

        threadLocal.set(mainThreadValue);

        Thread otherThread = new Thread(() -> {
            threadLocal.set(otherThreadValue);
            assertEquals("Value in other thread should be set correctly", otherThreadValue, threadLocal.get());
        });

        otherThread.start();
        otherThread.join();

        assertEquals("Value in main thread should remain unchanged", mainThreadValue, threadLocal.get());
    }

    @Test
    public void testPurge() throws InterruptedException {
        String value = "value";
        threadLocal.set(value);

        Thread otherThread = new Thread(() -> {
            threadLocal.set("otherThreadValue");
        });

        otherThread.start();
        otherThread.join();
        otherThread = null;

        System.gc();
        Thread.sleep(100); // Allow GC to potentially collect the thread

        assertEquals("Value in main thread should remain unchanged after purge", value, threadLocal.get());
    }

    @Test
    public void testClose() {
        String testValue = "test";
        threadLocal.set(testValue);
        threadLocal.close();

        assertNull("Value should be null after close", threadLocal.get());
        assertNull("ThreadLocal reference should be null after close", threadLocal.t);
    }
}