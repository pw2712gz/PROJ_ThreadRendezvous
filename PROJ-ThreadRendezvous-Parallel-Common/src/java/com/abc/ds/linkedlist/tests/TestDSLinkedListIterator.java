package com.abc.ds.linkedlist.tests;

import com.abc.ds.iterator.*;
import com.abc.ds.linkedlist.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSLinkedListIterator extends TestDSLinkedListBase {
    public TestDSLinkedListIterator(DSLinkedListFactory factory) {
        super("DSLinkedList - createIterator()", factory);
    }

    @Override
    protected void performTests() {
        testOnEmpty();
        testOnOne();
        testOnTwo();
        testOnSeveral();
    }

    private void testOnEmpty() {
        outln(" - createIterator() on empty -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("isEmpty()", ll.isEmpty(), true);

        outln("createIterator()...");
        DSIterator<String> iterator = ll.createIterator();
        outln("iterator != null", iterator != null, true);
        outln("iterator.hasNext()", iterator.hasNext(), false);
    }

    private void testOnOne() {
        outln(" - createIterator() on one -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        outln("createIterator()...");
        DSIterator<String> iterator = ll.createIterator();
        outln("iterator.hasNext()", iterator.hasNext(), true);
        outln("iterator.next()", iterator.next(), "apple");
        outln("iterator.hasNext()", iterator.hasNext(), false);
    }

    private void testOnTwo() {
        outln(" - createIterator() on two -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana");
        outln("createIterator()...");
        DSIterator<String> iterator = ll.createIterator();
        outln("iterator.hasNext()", iterator.hasNext(), true);
        outln("iterator.next()", iterator.next(), "apple");
        outln("iterator.hasNext()", iterator.hasNext(), true);
        outln("iterator.next()", iterator.next(), "banana");
        outln("iterator.hasNext()", iterator.hasNext(), false);
    }

    private void testOnSeveral() {
        outln(" - createIterator() on several -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("adding some junk to be cleared before iterator test...");
        insertLast(ll, "JUNK A");
        insertLast(ll, "JUNK B");
        insertLast(ll, "JUNK C");
        outln("clear()...");
        ll.clear();

        String[] fruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(7);

        insertLast(ll, fruits);
        outln("createIterator()...");
        DSIterator<String> iterator = ll.createIterator();
        outln("iterator.hasNext()", iterator.hasNext(), true);
        for ( int i = 0; i < fruits.length; i++ ) {
            outln("iterator.next()", iterator.next(), fruits[i]);
        }
        outln("iterator.hasNext()", iterator.hasNext(), false);
    }
}
