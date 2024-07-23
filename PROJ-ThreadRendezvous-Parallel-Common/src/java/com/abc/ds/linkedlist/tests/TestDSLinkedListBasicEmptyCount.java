package com.abc.ds.linkedlist.tests;

import com.abc.ds.linkedlist.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSLinkedListBasicEmptyCount extends TestDSLinkedListBase {
    public TestDSLinkedListBasicEmptyCount(DSLinkedListFactory factory) {
        super("DSLinkedList - empty and count tests", factory);
    }

    @Override
    protected void performTests() {
        testInitialState();
        testAddJustOne();
        testAddTwo();
        testAddFive();
    }

    private void testInitialState() {
        DSLinkedList<String> ll = createDSLinkedList();
        outln("initial state: ");
        outln("checking isEmpty()", ll.isEmpty(), true);
        outln("checking count()", ll.count(), 0);
    }

    private void testAddJustOne() {
        outln(" - add just one -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, TestFruitGenerator.getFruits(1));
        outln("checking isEmpty()", ll.isEmpty(), false);
        outln("checking count()", ll.count(), 1);
    }

    private void testAddTwo() {
        outln(" - add two -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, TestFruitGenerator.getFruits(2));
        outln("checking isEmpty()", ll.isEmpty(), false);
        outln("checking count()", ll.count(), 2);
    }

    private void testAddFive() {
        outln(" - add five -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, TestFruitGenerator.getFruits(5));
        outln("checking isEmpty()", ll.isEmpty(), false);
        outln("checking count()", ll.count(), 5);
    }
}
