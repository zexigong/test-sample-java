package org.apache.lucene.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.Random;

public class TestBytesRefArray {

    private BytesRefArray bytesRefArray;
    private Counter bytesUsed;

    @Before
    public void setUp() {
        bytesUsed = Counter.newCounter();
        bytesRefArray = new BytesRefArray(bytesUsed);
    }

    @Test
    public void testAppendAndSize() {
        assertEquals(0, bytesRefArray.size());

        BytesRef ref1 = new BytesRef("abc");
        bytesRefArray.append(ref1);
        assertEquals(1, bytesRefArray.size());

        BytesRef ref2 = new BytesRef("def");
        bytesRefArray.append(ref2);
        assertEquals(2, bytesRefArray.size());
    }

    @Test
    public void testClear() {
        BytesRef ref1 = new BytesRef("abc");
        bytesRefArray.append(ref1);
        assertEquals(1, bytesRefArray.size());

        bytesRefArray.clear();
        assertEquals(0, bytesRefArray.size());
    }

    @Test
    public void testGet() {
        BytesRefBuilder spare = new BytesRefBuilder();

        BytesRef ref1 = new BytesRef("abc");
        bytesRefArray.append(ref1);
        BytesRef ref2 = new BytesRef("def");
        bytesRefArray.append(ref2);

        assertEquals(ref1, bytesRefArray.get(spare, 0));
        assertEquals(ref2, bytesRefArray.get(spare, 1));
    }

    @Test
    public void testSort() {
        BytesRefBuilder spare = new BytesRefBuilder();
        Comparator<BytesRef> comp = Comparator.naturalOrder();

        bytesRefArray.append(new BytesRef("xyz"));
        bytesRefArray.append(new BytesRef("abc"));
        bytesRefArray.append(new BytesRef("def"));

        SortState sortState = bytesRefArray.sort(comp, false);
        IndexedBytesRefIterator it = bytesRefArray.iterator(sortState);

        assertEquals(new BytesRef("abc"), it.next());
        assertEquals(new BytesRef("def"), it.next());
        assertEquals(new BytesRef("xyz"), it.next());
        assertNull(it.next());
    }

    @Test
    public void testIteratorOrder() {
        bytesRefArray.append(new BytesRef("first"));
        bytesRefArray.append(new BytesRef("second"));
        bytesRefArray.append(new BytesRef("third"));

        BytesRefIterator it = bytesRefArray.iterator();
        assertEquals(new BytesRef("first"), it.next());
        assertEquals(new BytesRef("second"), it.next());
        assertEquals(new BytesRef("third"), it.next());
        assertNull(it.next());
    }

    @Test
    public void testRandomInsertions() {
        Random random = new Random();
        int numInsertions = 1000;

        for (int i = 0; i < numInsertions; i++) {
            byte[] bytes = new byte[10];
            random.nextBytes(bytes);
            bytesRefArray.append(new BytesRef(bytes));
        }

        assertEquals(numInsertions, bytesRefArray.size());
    }

    @Test
    public void testBytesUsedCounter() {
        long initialBytesUsed = bytesUsed.get();
        bytesRefArray.append(new BytesRef("abc"));
        bytesRefArray.append(new BytesRef("def"));
        assertTrue(bytesUsed.get() > initialBytesUsed);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        BytesRefBuilder spare = new BytesRefBuilder();
        bytesRefArray.get(spare, 0);
    }
}