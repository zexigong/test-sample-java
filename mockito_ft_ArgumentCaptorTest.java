/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.util.Primitives;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ArgumentCaptorTest {

    @BeforeEach
    public void initMocks() {
        openMocks(this);
    }

    @Test
    public void shouldCapturePrimitive() {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(int.class);

        assertThat(Primitives.isPrimitiveOrWrapper(captor.getCaptorType())).isTrue();
    }

    @Test
    public void shouldCapturePrimitiveWrapper() {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        assertThat(Primitives.isPrimitiveOrWrapper(captor.getCaptorType())).isTrue();
    }

    @Test
    public void shouldCaptureRawClass() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        assertThat(captor.getCaptorType()).isEqualTo(String.class);
    }

    @Test
    public void shouldCaptureGenericClass() {
        ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);

        assertThat(captor.getCaptorType()).isEqualTo(List.class);
    }

    @Test
    public void shouldCapturePrimitiveWithVarArgs() {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.captor();

        assertThat(Primitives.isPrimitiveOrWrapper(captor.getCaptorType())).isTrue();
    }

    @Test
    public void shouldCapturePrimitiveWrapperWithVarArgs() {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.captor();

        assertThat(Primitives.isPrimitiveOrWrapper(captor.getCaptorType())).isTrue();
    }

    @Test
    public void shouldCaptureRawClassWithVarArgs() {
        ArgumentCaptor<String> captor = ArgumentCaptor.captor();

        assertThat(captor.getCaptorType()).isEqualTo(String.class);
    }

    @Test
    public void shouldCaptureGenericClassWithVarArgs() {
        ArgumentCaptor<List<String>> captor = ArgumentCaptor.captor();

        assertThat(captor.getCaptorType()).isEqualTo(List.class);
    }

    @Test
    public void shouldCaptureGenericTypeWithVarArgs() {
        // Given
        UserRepository repository = Mockito.mock();
        UserService service = new UserService(repository);

        Map<String, User> expectedUsers =
                Map.of("12345", new User("12345", "Bob"), "45678", new User("45678", "Dave"));

        ArgumentCaptor<Map<String, User>> captor = ArgumentCaptor.captor();

        doNothing().when(repository).storeUsers(captor.capture());

        // When
        service.createUsers(List.of(new User("12345", "Bob"), new User("45678", "Dave")));

        // Then
        Map<String, User> actualUsers = captor.getValue();

        assertThat(expectedUsers).isEqualTo(actualUsers);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUsingCaptorWithVarArgs() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ArgumentCaptor.captor(1));
    }

    @Test
    public void shouldCaptureAllValues() {
        // Given
        UserRepository repository = Mockito.mock();
        UserService service = new UserService(repository);

        List<User> expectedUsers =
                List.of(new User("12345", "Bob"), new User("45678", "Dave"));

        ArgumentCaptor<User> captor = ArgumentCaptor.captor();

        doNothing().when(repository).storeUser(captor.capture());

        // When
        service.createUsers(expectedUsers);

        // Then
        List<User> actualUsers = captor.getAllValues();

        assertThat(expectedUsers).isEqualTo(actualUsers);
    }

    @Test
    public void shouldCaptureAllValuesWithMultipleInvocations() {
        // Given
        UserRepository repository = Mockito.mock();
        UserService service = new UserService(repository);

        List<User> expectedUsers =
                List.of(new User("12345", "Bob"), new User("45678", "Dave"));

        ArgumentCaptor<User> captor = ArgumentCaptor.captor();

        doNothing().when(repository).storeUser(captor.capture());

        // When
        service.createUsers(List.of(new User("12345", "Bob")));
        service.createUsers(List.of(new User("45678", "Dave")));

        // Then
        List<User> actualUsers = captor.getAllValues();

        assertThat(expectedUsers).isEqualTo(actualUsers);
    }

    private interface UserRepository {

        void storeUser(User user);

        void storeUsers(Map<String, User> users);
    }

    private static class UserService {

        private final UserRepository repository;

        public UserService(UserRepository repository) {
            this.repository = repository;
        }

        public void createUsers(List<User> users) {
            users.forEach(user -> repository.storeUser(user));
        }
    }

    private static class User {

        private final String id;

        private final String name;

        public User(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (obj instanceof User) {
                return id.equals(((User) obj).id);
            }
            return false;
        }
    }
}