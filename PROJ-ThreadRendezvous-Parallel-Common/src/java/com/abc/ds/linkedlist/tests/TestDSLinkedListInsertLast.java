package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;

/* deliberate package access */
class TestDSLinkedListInsertLast extends TestDSLinkedListBase {
    public TestDSLinkedListInsertLast(DSLinkedListFactory factory) {
        super("DSLinkedList - insertLast()", factory);
    }

    @Override
    protected void performTests() {
        testInsertLastWithTwoInitial();
        testInsertLastMultipleWithTwoInitial();
        testInsertLastMultipleWithOneInitial();
        testInsertLastInitiallyEmpty();
        testInsertLastMultipleInitiallyEmpty();
    }

    private void testInsertLastWithTwoInitial() {
        outln(" - insertLast() with two initial-");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana");
        outln("ll.insertLast(\"kiwi\")");
        ll.insertLast("kiwi");
        confirmPeekAll(ll, "apple", "banana", "kiwi");
    }

    private void testInsertLastMultipleWithTwoInitial() {
        outln(" - insertLast() multiple times with two initial-");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana");
        outln("ll.insertLast(\"kiwi\")");
        ll.insertLast("kiwi");
        outln("ll.insertLast(\"grape\")");
        ll.insertLast("grape");
        outln("ll.insertLast(\"orange\")");
        ll.insertLast("orange");
        confirmPeekAll(ll, "apple", "banana", "kiwi", "grape", "orange");
    }

    private void testInsertLastMultipleWithOneInitial() {
        outln(" - insertLast() multiple times with one initial-");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        outln("ll.insertLast(\"kiwi\")");
        ll.insertLast("kiwi");
        outln("ll.insertLast(\"grape\")");
        ll.insertLast("grape");
        outln("ll.insertLast(\"orange\")");
        ll.insertLast("orange");
        confirmPeekAll(ll, "apple", "kiwi", "grape", "orange");
    }

    private void testInsertLastInitiallyEmpty() {
        outln(" - insertLast() on initially empty -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("ll.insertLast(\"kiwi\")");
        ll.insertLast("kiwi");
        confirmPeekAll(ll, "kiwi");
    }

    private void testInsertLastMultipleInitiallyEmpty() {
        outln(" - insertLast() multiple times on initially empty -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("ll.insertLast(\"kiwi\")");
        ll.insertLast("kiwi");
        outln("ll.insertLast(\"grape\")");
        ll.insertLast("grape");
        outln("ll.insertLast(\"orange\")");
        ll.insertLast("orange");
        confirmPeekAll(ll, "kiwi", "grape", "orange");
    }
}
