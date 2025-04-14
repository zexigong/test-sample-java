/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

class ArgumentCaptorTest {

    @Test
    void shouldCaptureVarargs() {
        // given
        var repository = mock(UserRepository.class);
        var service = new UserService(repository);

        var expectedUsers =
                Map.of("12345", new User("12345", "Bob"), "45678", new User("45678", "Dave"));

        var captor = ArgumentCaptor.<Map<String, User>>forClass(Map.class);

        doNothing().when(repository).storeUsers(captor.capture());

        // when
        service.createUsers(
                new User[] {new User("12345", "Bob"), new User("45678", "Dave")});

        // then
        var actualUsers = captor.getValue();

        assertThat(actualUsers).isEqualTo(expectedUsers);
    }

    @Test
    void shouldCaptureVarargsUsingCaptor() {
        // given
        var repository = mock(UserRepository.class);
        var service = new UserService(repository);

        var expectedUsers =
                Map.of("12345", new User("12345", "Bob"), "45678", new User("45678", "Dave"));

        var captor = ArgumentCaptor.captor();

        doNothing().when(repository).storeUsers(captor.capture());

        // when
        service.createUsers(
                new User[] {new User("12345", "Bob"), new User("45678", "Dave")});

        // then
        var actualUsers = captor.getValue();

        assertThat(actualUsers).isEqualTo(expectedUsers);
    }

    @Test
    void shouldCaptureVarargsUsingCaptorWithNestedGenericTypes() {
        // given
        var repository = mock(UserRepository.class);
        var service = new UserService(repository);

        var expectedUsers =
                Map.of("12345", new User("12345", "Bob"), "45678", new User("45678", "Dave"));

        var captor = ArgumentCaptor.<Map<String, User>>captor();

        doNothing().when(repository).storeUsers(captor.capture());

        // when
        service.createUsers(
                new User[] {new User("12345", "Bob"), new User("45678", "Dave")});

        // then
        var actualUsers = captor.getValue();

        assertThat(actualUsers).isEqualTo(expectedUsers);
    }

    @Test
    void shouldNotAcceptAnyArgumentsInCaptor() {
        // given
        var repository = mock(UserRepository.class);
        var service = new UserService(repository);

        var expectedUsers =
                Map.of("12345", new User("12345", "Bob"), "45678", new User("45678", "Dave"));

        assertThatIllegalArgumentException()
                // when
                .isThrownBy(() -> ArgumentCaptor.captor(Map.of("12345", new User("12345", "Bob"))))
                // then
                .withMessage("Do not provide any arguments to the 'captor' call");
    }

    static class User {
        private final String id;
        private final String name;

        User(String id, String name) {
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
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            var other = (User) obj;
            return id.equals(other.id) && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            var result = id.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class UserRepository {
        private final Map<String, User> users = new ConcurrentHashMap<>();

        void storeUsers(Map<String, User> users) {
            this.users.putAll(users);
        }
    }

    static class UserService {
        private final UserRepository repository;

        UserService(UserRepository repository) {
            this.repository = repository;
        }

        void createUsers(User... users) {
            var userMap = new HashMap<String, User>();
            for (User user : users) {
                userMap.put(user.getId(), user);
            }
            repository.storeUsers(userMap);
        }
    }
}