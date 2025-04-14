/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.verification.api.VerificationData;

class ThreadSafeMockingProgressTest {

    @Test
    void threadSafeMockingProgress() throws Exception {
        MockingProgress p1 = ThreadSafeMockingProgress.mockingProgress();
        MockingProgress p2 = ThreadSafeMockingProgress.mockingProgress();
        assertThat(p1).isSameAs(p2);

        Thread t =
                new Thread(
                        () -> {
                            MockingProgress p3 = ThreadSafeMockingProgress.mockingProgress();
                            MockingProgress p4 = ThreadSafeMockingProgress.mockingProgress();
                            assertThat(p3).isSameAs(p4);
                            assertThat(p3).isNotSameAs(p1);
                        });
        t.start();
        t.join();
    }

    @Test
    void threadLocalIsResetWhenArgumentAreDifferentIsThrown() {
        MockingProgress first = ThreadSafeMockingProgress.mockingProgress();
        try {
            throw new ArgumentsAreDifferent("testing the reset", new VerificationData() {
                @Override
                public Object getTarget() {
                    return null;
                }
            });
        } catch (ArgumentsAreDifferent ignored) {
        }

        assertThat(ThreadSafeMockingProgress.mockingProgress()).isNotSameAs(first);
    }
}