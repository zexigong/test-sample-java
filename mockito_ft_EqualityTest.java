/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void shouldReturnFalseIfDifferentObjectTypes() {
        assertFalse(Equality.areEqual(new Object(), new Object() {}));
    }

    @Test
    public void shouldReturnTrueIfSameObject() {
        Object o = new Object();
        assertTrue(Equality.areEqual(o, o));
    }

    @Test
    public void shouldReturnFalseIfOneIsNullAndAnotherIsNot() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void shouldReturnTrueIfTwoNulls() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void shouldReturnFalseIfDifferentPrimitiveArrays() {
        assertFalse(Equality.areEqual(new boolean[] {false}, new boolean[] {true}));
        assertFalse(Equality.areEqual(new byte[] {1}, new byte[] {2}));
        assertFalse(Equality.areEqual(new char[] {1}, new char[] {2}));
        assertFalse(Equality.areEqual(new short[] {1}, new short[] {2}));
        assertFalse(Equality.areEqual(new int[] {1}, new int[] {2}));
        assertFalse(Equality.areEqual(new long[] {1}, new long[] {2}));
        assertFalse(Equality.areEqual(new float[] {1}, new float[] {2}));
        assertFalse(Equality.areEqual(new double[] {1}, new double[] {2}));
    }

    @Test
    public void shouldReturnTrueIfEqualPrimitiveArrays() {
        assertTrue(Equality.areEqual(new boolean[] {true}, new boolean[] {true}));
        assertTrue(Equality.areEqual(new byte[] {1}, new byte[] {1}));
        assertTrue(Equality.areEqual(new char[] {1}, new char[] {1}));
        assertTrue(Equality.areEqual(new short[] {1}, new short[] {1}));
        assertTrue(Equality.areEqual(new int[] {1}, new int[] {1}));
        assertTrue(Equality.areEqual(new long[] {1}, new long[] {1}));
        assertTrue(Equality.areEqual(new float[] {1}, new float[] {1}));
        assertTrue(Equality.areEqual(new double[] {1}, new double[] {1}));
    }

    @Test
    public void shouldReturnTrueIfEqualObjectArrays() {
        assertTrue(Equality.areEqual(new String[] {"1"}, new String[] {"1"}));
    }

    @Test
    public void shouldReturnFalseIfDifferentObjectArrays() {
        assertFalse(Equality.areEqual(new String[] {"1"}, new String[] {"2"}));
    }

    @Test
    public void shouldReturnFalseIfDifferentArrayLengths() {
        assertFalse(Equality.areEqual(new String[] {"1"}, new String[] {"1", "2"}));
    }
}