package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSackIterator extends TestDSSackBase {
    public TestDSSackIterator(DSSackFactory factory) {
        super("createIterator()", factory);
    }

    @Override
    protected void performTests() {
        testIteratorOnOne();
        testIteratorOnTwo();
        testIteratorOnSeveral();
        testIteratorOnEmpty();
    }

    private void testIteratorOnEmpty() {
        outln(" - createIterator() on empty -");
        DSSack<String> ds = createDS();
        checkIsEmpty(ds, true);
        checkIterator(ds, StringTools.ZERO_LEN_ARRAY);
    }

    private void testIteratorOnOne() {
        outln(" - createIterator() on one -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        checkIterator(ds, "apple");
    }

    private void testIteratorOnTwo() {
        outln(" - createIterator() on two -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        checkIterator(ds, "apple", "banana");
    }

    private void testIteratorOnSeveral() {
        outln(" - createIterator() on several -");
        DSSack<String> ds = createDS();
        outln("adding some junk to be cleared before peekAll() test...");
        add(ds, "JUNK A");
        add(ds, "JUNK B");
        add(ds, "JUNK C");
        outln("clear()...");
        ds.clear();

        String[] fruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, fruits);
        checkIterator(ds, fruits);
    }
}
