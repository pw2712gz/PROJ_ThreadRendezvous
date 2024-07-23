package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSLinkedListPeekAll extends TestDSLinkedListBase {
    public TestDSLinkedListPeekAll(DSLinkedListFactory factory) {
        super("DSLinkedList - peekAll()", factory);
    }

    @Override
    protected void performTests() {
        testPeekAllOnEmpty();
        testPeekAllOnOne();
        testPeekAllOnTwo();
        testPeekAllOnSeveral();
    }

    private void testPeekAllOnEmpty() {
        outln(" - peekAll() on empty -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("isEmpty()", ll.isEmpty(), true);

        confirmPeekAll(ll, StringTools.ZERO_LEN_ARRAY);
    }

    private void testPeekAllOnOne() {
        outln(" - peekAll() on one -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        confirmPeekAll(ll, "apple");
    }

    private void testPeekAllOnTwo() {
        outln(" - peekAll() on two -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        insertLast(ll, "banana");
        confirmPeekAll(ll, "apple", "banana");
    }

    private void testPeekAllOnSeveral() {
        outln(" - peekAll() on several -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("adding some junk to be cleared before peekAll() test...");
        insertLast(ll, "JUNK A");
        insertLast(ll, "JUNK B");
        insertLast(ll, "JUNK C");
        outln("clear()...");
        ll.clear();

        String[] fruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(20);

        insertLast(ll, fruits);
        confirmPeekAll(ll, fruits);
    }
}
