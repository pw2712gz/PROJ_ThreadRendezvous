package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSSackPeekAll extends TestDSSackBase {
    public TestDSSackPeekAll(DSSackFactory factory) {
        super("peekAll()", factory);
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
        DSSack<String> ds = createDS();
        checkIsEmpty(ds, true);
        checkPeekAllOnEmpty(ds);
    }

    private void testPeekAllOnOne() {
        outln(" - peekAll() on one -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        checkPeekAll(ds, "apple");
    }

    private void testPeekAllOnTwo() {
        outln(" - peekAll() on two -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        checkPeekAll(ds, "apple", "banana");
    }

    private void testPeekAllOnSeveral() {
        outln(" - peekAll() on several -");
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
        checkPeekAll(ds, fruits);
    }
}
