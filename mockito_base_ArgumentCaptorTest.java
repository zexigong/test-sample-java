package org.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.CapturingMatcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArgumentCaptorTest {

    private MockedClass mock;

    static class MockedClass {
        void doSomething(Person person) {}
        void varArgMethod(Person... people) {}
    }

    static class Person {
        private final String name;

        Person(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Person person = (Person) obj;
            return name.equals(person.name);
        }
    }

    @BeforeEach
    void setUp() {
        mock = mock(MockedClass.class);
    }

    @Test
    void testCaptureSingleArgument() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);

        mock.doSomething(new Person("John"));
        verify(mock).doSomething(captor.capture());

        assertEquals("John", captor.getValue().getName());
    }

    @Test
    void testCaptureVarArgs() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);

        mock.varArgMethod(new Person("John"), new Person("Jane"));
        verify(mock).varArgMethod(captor.capture());

        List<Person> captured = captor.getAllValues();
        assertEquals(2, captured.size());
        assertEquals("John", captured.get(0).getName());
        assertEquals("Jane", captured.get(1).getName());
    }

    @Test
    void testCaptureMultipleInvocations() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);

        mock.doSomething(new Person("John"));
        mock.doSomething(new Person("Jane"));
        verify(mock, times(2)).doSomething(captor.capture());

        List<Person> captured = captor.getAllValues();
        assertEquals(2, captured.size());
        assertEquals("John", captured.get(0).getName());
        assertEquals("Jane", captured.get(1).getName());
    }

    @Test
    void testGetValueThrowsExceptionIfNoValueCaptured() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);

        Exception exception = assertThrows(IllegalArgumentException.class, captor::getValue);
        assertEquals("Argument(s) are different! Wanted but not invoked", exception.getMessage());
    }

    @Test
    void testCaptorType() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        assertEquals(Person.class, captor.getCaptorType());
    }

    @Test
    void testCaptorWithNoArgumentsThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ArgumentCaptor.captor(new String[0]));
        assertEquals("Do not provide any arguments to the 'captor' call", exception.getMessage());
    }
}