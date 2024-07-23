package com.abc.ds.linkedlist.tests;

import java.util.*;

import com.abc.ds.linkedlist.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSLinkedListPeekFirstLast extends TestDSLinkedListBase {
    public TestDSLinkedListPeekFirstLast(DSLinkedListFactory factory) {
        super("DSLinkedList - peekFirst() and peekLast()", factory);
    }

    @Override
    protected void performTests() {
        testOneNode();
        testTwoNodes();
        testSeveralNodes();
        testEmpty();
    }

    private void testOneNode() {
        outln(" - one node -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        outln("peekFirst().getPayload()", ll.peekFirst().getPayload(), "apple");
        outln("peekLast().getPayload()", ll.peekLast().getPayload(), "apple");
    }

    private void testTwoNodes() {
        outln(" - two nodes -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        insertLast(ll, "banana");
        outln("peekFirst().getPayload()", ll.peekFirst().getPayload(), "apple");
        outln("peekLast().getPayload()", ll.peekLast().getPayload(), "banana");
    }

    private void testSeveralNodes() {
        outln(" - several nodes -");
        DSLinkedList<String> ll = createDSLinkedList();
        String[] fruits = TestFruitGenerator.getFruits(15);
        for ( String fruit : fruits ) {
            insertLast(ll, fruit);
        }
        outln("peekFirst().getPayload()", ll.peekFirst().getPayload(), fruits[0]);
        outln("peekLast().getPayload()", ll.peekLast().getPayload(), fruits[fruits.length - 1]);
    }

    private void testEmpty() {
        outln(" - empty (NoSuchElementException) -");
        DSLinkedList<String> ll = createDSLinkedList();

        outln("confirming isEmpty()", ll.isEmpty(), true);
        if ( !ll.isEmpty() ) {
            outlnErrorText("Cannot proceed with peek on empty list tests: " +
                "newly created list should be empty but is not");
            return;
        }

        testEmptyHelper(ll, true);
        testEmptyHelper(ll, false);

        insertLast(ll, TestFruitGenerator.getFruits(5));
        outln("clear()");
        ll.clear();

        testEmptyHelper(ll, true);
        testEmptyHelper(ll, false);
    }

    private void testEmptyHelper(DSLinkedList<?> ll, boolean doPeekFirst) {
        if ( !ll.isEmpty() ) {
            outln("can't attempt - isEmpty() is not returning true", false);
            return;
        }

        String methodText = doPeekFirst ? "peekFirst()" : "peekLast";

        boolean success = false;
        try {
            outln("list is empty, trying " + methodText + "...");
            if ( doPeekFirst ) {
                ll.peekFirst();
            } else {
                ll.peekLast();
            }
        } catch ( NoSuchElementException x ) {
            outln("expected this exception: " + x.toString());
            success = true;
        } catch ( Exception x ) {
            failureExceptionWithStackTrace(x);
        }

        if ( success ) {
            outln(methodText + " on an empty list " +
                "threw NoSuchElementException", true);
        } else {
            outln(methodText + " on an empty list " +
                "did NOT throw NoSuchElementException", false);
        }
    }
}
