package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSackRemoveAll extends TestDSSackBase {
    public TestDSSackRemoveAll(DSSackFactory factory) {
        super("removeAll()", factory);
    }

    @Override
    protected void performTests() {
        testPeekAllOnEmpty();
        testPeekAllOnOne();
        testPeekAllOnTwo();
        testPeekAllOnSeveral();
    }

    private void testPeekAllOnEmpty() {
        outln(" - removeAll() on empty -");
        DSSack<String> ds = createDS();
        checkIsEmpty(ds, true);
        checkRemoveAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    private void testPeekAllOnOne() {
        outln(" - removeAll() on one -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        checkRemoveAll(ds, "apple");
        checkCount(ds, 0);
    }

    private void testPeekAllOnTwo() {
        outln(" - removeAll() on two -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        checkCount(ds, 2);
        checkRemoveAll(ds, "apple", "banana");
        checkCount(ds, 0);
    }

    private void testPeekAllOnSeveral() {
        outln(" - removeAll() on several -");
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

        String[] expectedFruits =
            testHelper.potentiallyRemoveDuplicatesAndShuffle(fruits);

        checkCount(ds, expectedFruits.length);
        checkRemoveAll(ds, expectedFruits);
        checkCount(ds, 0);
    }
}
