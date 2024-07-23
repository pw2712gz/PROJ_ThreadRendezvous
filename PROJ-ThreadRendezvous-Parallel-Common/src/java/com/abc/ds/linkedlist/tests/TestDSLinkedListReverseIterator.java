package com.abc.ds.linkedlist.tests;

import com.abc.ds.iterator.*;
import com.abc.ds.linkedlist.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSLinkedListReverseIterator extends TestDSLinkedListBase {
    public TestDSLinkedListReverseIterator(DSLinkedListFactory factory) {
        super("DSLinkedList - createReverseIterator()", factory);
    }

    @Override
    protected void performTests() {
        testOnEmpty();
        testOnOne();
        testOnTwo();
        testOnSeveral();
    }

    private void testOnEmpty() {
        outln(" - createReverseIterator() on empty -");
        DSLinkedList<String> ll = createDSLinkedList();
        outln("isEmpty()", ll.isEmpty(), true);

        outln("createReverseIterator()...");
        DSIterator<String> iterator = ll.createReverseIterator();
        outln("iterator != null", iterator != null, true);
        outln("iterator.hasNext()", iterator.hasNext(), false);
    }

    private void testOnOne() {
        outln(" - createReverseIterator() on one -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple");
        outln("createReverseIterator()...");
        DSIterator<String> iterator = ll.createReverseIterator();
        outln("iterator.hasNext()", iterator.hasNext(), true);
        outln("iterator.next()", iterator.next(), "apple");
        outln("iterator.hasNext()", iterator.hasNext(), false);
    }

    private void testOnTwo() {
        outln(" - createReverseIterator() on two -");
        DSLinkedList<String> ll = createDSLinkedList();
        insertLast(ll, "apple", "banana");
        outln("createReverseIterator()...");
        DSIterator<String> iterator = ll.createReverseIterator();
        outln("iterator.hasNext()", iterator.hasNext(), true);
        outln("iterator.next()", iterator.next(), "banana");
        outln("iterator.hasNext()", iterator.hasNext(), true);
        outln("iterator.next()", iterator.next(), "apple");
        outln("iterator.hasNext()", iterator.hasNext(), false);
    }

    private void testOnSeveral() {
        outln(" - createReverseIterator() on several -");
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
        outln("createReverseIterator()...");
        DSIterator<String> iterator = ll.createReverseIterator();
        outln("iterator.hasNext()", iterator.hasNext(), true);
        for ( int i = fruits.length - 1; i >= 0; i--) {
            outln("iterator.next()", iterator.next(), fruits[i]);
        }
        outln("iterator.hasNext()", iterator.hasNext(), false);
    }
}
