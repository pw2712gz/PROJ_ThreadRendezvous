package com.abc.ds.sack.string.tests;

import com.abc.ds.sack.string.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestStringDSSackRemoveAll extends TestStringDSSackBase {
    public TestStringDSSackRemoveAll(StringDSSackFactory factory) {
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
        StringDSSack ds = createDS();
        checkIsEmpty(ds, true);
        checkRemoveAll(ds, StringTools.ZERO_LEN_ARRAY);
    }

    private void testPeekAllOnOne() {
        outln(" - removeAll() on one -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        checkRemoveAll(ds, "apple");
        checkCount(ds, 0);
    }

    private void testPeekAllOnTwo() {
        outln(" - removeAll() on two -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        checkCount(ds, 2);
        checkRemoveAll(ds, "apple", "banana");
        checkCount(ds, 0);
    }

    private void testPeekAllOnSeveral() {
        outln(" - removeAll() on several -");
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

        String[] expectedFruits =
            testHelper.potentiallyRemoveDuplicatesAndShuffle(fruits);

        checkCount(ds, expectedFruits.length);
        checkRemoveAll(ds, expectedFruits);
        checkCount(ds, 0);
    }
}
