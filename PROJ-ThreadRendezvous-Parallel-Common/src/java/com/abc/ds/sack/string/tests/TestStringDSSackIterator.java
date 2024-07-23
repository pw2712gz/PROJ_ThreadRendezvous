package com.abc.ds.sack.string.tests;

import com.abc.ds.sack.string.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestStringDSSackIterator extends TestStringDSSackBase {
    public TestStringDSSackIterator(StringDSSackFactory factory) {
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
        StringDSSack ds = createDS();
        checkIsEmpty(ds, true);
        checkIterator(ds, StringTools.ZERO_LEN_ARRAY);
    }

    private void testIteratorOnOne() {
        outln(" - createIterator() on one -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        checkIterator(ds, "apple");
    }

    private void testIteratorOnTwo() {
        outln(" - createIterator() on two -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        checkIterator(ds, "apple", "banana");
    }

    private void testIteratorOnSeveral() {
        outln(" - createIterator() on several -");
        StringDSSack ds = createDS();
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
