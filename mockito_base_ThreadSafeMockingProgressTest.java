package org.mockito.internal.progress;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ThreadSafeMockingProgressTest {

    @Test
    void testMockingProgressNotNull() {
        // Verify that mockingProgress() returns a non-null MockingProgress instance
        MockingProgress progress = ThreadSafeMockingProgress.mockingProgress();
        assertNotNull(progress, "MockingProgress should not be null");
    }

    @Test
    void testThreadLocalIsolation() throws InterruptedException {
        // Verify that different threads have different MockingProgress instances
        MockingProgress mainThreadProgress = ThreadSafeMockingProgress.mockingProgress();

        Thread thread = new Thread(() -> {
            MockingProgress threadProgress = ThreadSafeMockingProgress.mockingProgress();
            assertNotSame(mainThreadProgress, threadProgress, "Threads should have separate MockingProgress instances");
        });

        thread.start();
        thread.join();
    }
}