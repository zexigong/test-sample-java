package org.mockito.internal.matchers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EqualityTest {

    @Test
    void testAreEqualWithSameObject() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    void testAreEqualWithBothNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    void testAreEqualWithOneNull() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    void testAreEqualWithEqualObjects() {
        assertTrue(Equality.areEqual("test", new String("test")));
    }

    @Test
    void testAreEqualWithDifferentObjects() {
        assertFalse(Equality.areEqual("test", "different"));
    }

    @Test
    void testAreEqualWithEqualPrimitiveArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    void testAreEqualWithDifferentPrimitiveArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    void testAreEqualWithEqualObjectArrays() {
        String[] arr1 = {"a", "b", "c"};
        String[] arr2 = {"a", "b", "c"};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    void testAreEqualWithDifferentObjectArrays() {
        String[] arr1 = {"a", "b", "c"};
        String[] arr2 = {"a", "b", "d"};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    void testAreEqualWithArraysOfDifferentLengths() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    void testAreEqualWithDifferentArrayTypes() {
        int[] arr1 = {1, 2, 3};
        Object[] arr2 = {1, 2, 3};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    void testAreEqualWithNestedArrays() {
        int[][] arr1 = {{1, 2}, {3, 4}};
        int[][] arr2 = {{1, 2}, {3, 4}};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    void testAreEqualWithDifferentNestedArrays() {
        int[][] arr1 = {{1, 2}, {3, 4}};
        int[][] arr2 = {{1, 2}, {4, 3}};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    void testIsArrayWithArrayInput() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
    }

    @Test
    void testIsArrayWithNonArrayInput() {
        assertFalse(Equality.isArray("not an array"));
    }
}