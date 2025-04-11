/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ThreadSafeMockingProgressTest {

    @Test
    public void shouldProvideMockingProgress() {
        assertThat(ThreadSafeMockingProgress.mockingProgress()).isNotNull();
    }

    @Test
    public void shouldProvideThreadIndependentMockingProgress() throws InterruptedException {
        MockingProgress[] result = new MockingProgress[1];
        Thread t = new Thread(() -> result[0] = ThreadSafeMockingProgress.mockingProgress());
        t.start();
        t.join();
        assertThat(ThreadSafeMockingProgress.mockingProgress()).isNotSameAs(result[0]);
    }
}