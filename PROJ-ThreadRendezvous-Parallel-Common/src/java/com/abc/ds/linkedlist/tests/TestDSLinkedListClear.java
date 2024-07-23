package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSLinkedListClear extends TestDSLinkedListBase {
    public TestDSLinkedListClear(DSLinkedListFactory factory) {
        super("DSLinkedList - clear tests", factory);
    }

    @Override
    protected void performTests() {
        testAddThenClear();
        testAddThenClearThenAdd();
    }

    private void testAddThenClear() {
        outln(" - add, then clear -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("adding 5:");
        insertLast(ll, TestFruitGenerator.getFruits(0, 5));
        outln("checking count()", ll.count(), 5);
        outln("clear()");
        ll.clear();
        outln("checking isEmpty()", ll.isEmpty(), true);
        outln("checking count()", ll.count(), 0);
    }

    private void testAddThenClearThenAdd() {
        outln(" - add, then clear, then add -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("adding 20:");
        insertLast(ll, TestFruitGenerator.getFruits(0, 20));
        outln("checking count()", ll.count(), 20);

        outln("clear()");
        ll.clear();
        outln("checking count()", ll.count(), 0);
        outln("checking isEmpty()", ll.isEmpty(), true);

        outln("adding 6:");
        insertLast(ll, TestFruitGenerator.getFruits(20, 6));
        outln("checking count()", ll.count(), 6);
        outln("checking isEmpty()", ll.isEmpty(), false);
    }
}
