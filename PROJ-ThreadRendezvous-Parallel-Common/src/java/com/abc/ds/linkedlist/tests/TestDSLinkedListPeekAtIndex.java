package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSLinkedListPeekAtIndex extends TestDSLinkedListBase {
    public TestDSLinkedListPeekAtIndex(DSLinkedListFactory factory) {
        super("DSLinkedList - peekAtIndex()", factory);
    }

    @Override
    protected void performTests() {
        testOneNode();
        testTwoNodes();
        testSeveralNodes();
        testBadIndex();
    }

    private void testOneNode() {
        outln(" - one node -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        outln("peekAtIndex(0).getPayload()", ll.peekAtIndex(0).getPayload(), "apple");
    }

    private void testTwoNodes() {
        outln(" - two nodes -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        insertLast(ll, "banana");
        outln("peekAtIndex(0).getPayload()", ll.peekAtIndex(0).getPayload(), "apple");
        outln("peekAtIndex(1).getPayload()", ll.peekAtIndex(1).getPayload(), "banana");
    }

    private void testSeveralNodes() {
        outln(" - several nodes -");
        DSLinkedList<String> ll = createDSLinkedList();
        String[] fruits = TestFruitGenerator.getFruits(15);
        for ( String fruit : fruits ) {
            insertLast(ll, fruit);
        }

        for ( int idx : new int[]{ 0, 1, 5, 10, fruits.length - 2, fruits.length - 1 } ) {
            outln("peekAtIndex(" + idx + ").getPayload()", ll.peekAtIndex(idx).getPayload(), fruits[idx]);
        }
    }

    private void testBadIndex() {
        outln(" - bad index -");
        DSLinkedList<String> ll = createDSLinkedList();
        String[] fruits = TestFruitGenerator.getFruits(10);
        for ( String fruit : fruits ) {
            insertLast(ll, fruit);
        }

        for ( int idx : new int[]{ -1, fruits.length, fruits.length + 1 } ) {
            testBadIndexHelper(ll, idx);
        }
    }

    private void testBadIndexHelper(DSLinkedList<?> ll, int index) {
        boolean success = false;
        try {
            outln("count() is " + ll.count() +
                ", trying peekAtIndex(" + index + ")...");
            ll.peekAtIndex(index);
        } catch ( IndexOutOfBoundsException x ) {
            outln("expected this exception: " + x.toString());
            success = true;
        } catch ( Exception x ) {
            failureExceptionWithStackTrace(x);
        }

        if ( success ) {
            outln("peekAtIndex(" + index +
                ") threw IndexOutOfBoundsException", true);
        } else {
            outln("peekAtIndex(" + index +
                ") did NOT throw IndexOutOfBoundsException", false);
        }
    }
}
