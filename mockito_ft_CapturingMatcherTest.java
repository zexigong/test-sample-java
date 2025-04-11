/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.exceptions.Reporter.noArgumentValueWasCaptured;

import java.util.List;
import java.util.concurrent.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CapturingMatcherTest {

    private CapturingMatcher<Integer> matcher = new CapturingMatcher<>(Integer.class);

    @Test
    void shouldCaptureArguments() {
        // when
        matcher.captureFrom(1);
        matcher.captureFrom(200);

        // then
        List<Integer> values = matcher.getAllValues();
        assertThat(values).containsExactly(1, 200);
    }

    @Test
    void shouldCaptureNullArguments() {
        // when
        matcher.captureFrom(null);

        // then
        List<Integer> values = matcher.getAllValues();
        assertThat(values).containsExactly((Integer) null);
    }

    @Test
    void shouldGetLastValue() {
        // when
        matcher.captureFrom(1);
        matcher.captureFrom(200);

        // then
        Integer lastValue = matcher.getLastValue();
        assertThat(lastValue).isEqualTo(200);
    }

    @Test
    void shouldGetLastValueWhenNull() {
        // when
        matcher.captureFrom(null);

        // then
        Integer lastValue = matcher.getLastValue();
        assertThat(lastValue).isNull();
    }

    @Test
    void shouldGetLastValueWhenOnlyNull() {
        // when
        matcher.captureFrom(1);
        matcher.captureFrom(null);

        // then
        Integer lastValue = matcher.getLastValue();
        assertThat(lastValue).isNull();
    }

    @Test
    void shouldGetLastValueWhenEmpty() {
        // then
        Assertions.assertThatThrownBy(() -> matcher.getLastValue())
                .isInstanceOf(noArgumentValueWasCaptured().getClass())
                .hasMessageContaining(noArgumentValueWasCaptured().getMessage());
    }

    @Test
    void shouldCaptureArgumentsInMultithreadedEnvironment() throws InterruptedException, ExecutionException {
        // given
        ExecutorService service = Executors.newFixedThreadPool(100);

        // when
        Future<Boolean>[] futures = new Future[1000];
        for (int i = 0; i < futures.length; i++) {
            futures[i] =
                    service.submit(
                            () -> {
                                matcher.captureFrom(200);
                                return true;
                            });
        }

        for (Future<Boolean> future : futures) {
            future.get();
        }

        // then
        List<Integer> values = matcher.getAllValues();
        assertThat(values).hasSize(1000);
    }

    @Test
    void shouldGetLastValueInMultithreadedEnvironment() throws InterruptedException, ExecutionException {
        // given
        ExecutorService service = Executors.newFixedThreadPool(100);

        // when
        Future<Boolean>[] futures = new Future[1000];
        for (int i = 0; i < futures.length; i++) {
            futures[i] =
                    service.submit(
                            () -> {
                                matcher.getLastValue();
                                return true;
                            });
        }

        for (Future<Boolean> future : futures) {
            future.get();
        }
    }

    @Test
    void shouldGetAllValuesInMultithreadedEnvironment() throws InterruptedException, ExecutionException {
        // given
        ExecutorService service = Executors.newFixedThreadPool(100);

        // when
        Future<Boolean>[] futures = new Future[1000];
        for (int i = 0; i < futures.length; i++) {
            futures[i] =
                    service.submit(
                            () -> {
                                matcher.getAllValues();
                                return true;
                            });
        }

        for (Future<Boolean> future : futures) {
            future.get();
        }
    }
}