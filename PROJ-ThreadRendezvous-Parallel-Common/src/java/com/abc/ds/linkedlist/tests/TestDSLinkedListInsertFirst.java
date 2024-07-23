package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;

/* deliberate package access */
class TestDSLinkedListInsertFirst extends TestDSLinkedListBase {
    public TestDSLinkedListInsertFirst(DSLinkedListFactory factory) {
        super("DSLinkedList - insertFirst()", factory);
    }

    @Override
    protected void performTests() {
        testInsertFirstWithTwoInitial();
        testInsertFirstMultipleWithTwoInitial();
        testInsertFirstMultipleWithOneInitial();
        testInsertFirstInitiallyEmpty();
        testInsertFirstMultipleInitiallyEmpty();
    }

    private void testInsertFirstWithTwoInitial() {
        outln(" - insertFirst() with two initial-");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana");
        outln("ll.insertFirst(\"kiwi\")");
        ll.insertFirst("kiwi");
        confirmPeekAll(ll, "kiwi", "apple", "banana");
    }

    private void testInsertFirstMultipleWithTwoInitial() {
        outln(" - insertFirst() multiple times with two initial-");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana");
        outln("ll.insertFirst(\"kiwi\")");
        ll.insertFirst("kiwi");
        outln("ll.insertFirst(\"grape\")");
        ll.insertFirst("grape");
        outln("ll.insertFirst(\"orange\")");
        ll.insertFirst("orange");
        confirmPeekAll(ll, "orange", "grape", "kiwi", "apple", "banana");
    }

    private void testInsertFirstMultipleWithOneInitial() {
        outln(" - insertFirst() multiple times with one initial-");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        outln("ll.insertFirst(\"kiwi\")");
        ll.insertFirst("kiwi");
        outln("ll.insertFirst(\"grape\")");
        ll.insertFirst("grape");
        outln("ll.insertFirst(\"orange\")");
        ll.insertFirst("orange");
        confirmPeekAll(ll, "orange", "grape", "kiwi", "apple");
    }

    private void testInsertFirstInitiallyEmpty() {
        outln(" - insertFirst() on initially empty -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("ll.insertFirst(\"kiwi\")");
        ll.insertFirst("kiwi");
        confirmPeekAll(ll, "kiwi");
    }

    private void testInsertFirstMultipleInitiallyEmpty() {
        outln(" - insertFirst() multiple times on initially empty -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("ll.insertFirst(\"kiwi\")");
        ll.insertFirst("kiwi");
        outln("ll.insertFirst(\"grape\")");
        ll.insertFirst("grape");
        outln("ll.insertFirst(\"orange\")");
        ll.insertFirst("orange");
        confirmPeekAll(ll, "orange", "grape", "kiwi");
    }
}
