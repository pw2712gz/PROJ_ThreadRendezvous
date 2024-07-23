package com.abc.ds.sack.string.tests;

import com.abc.ds.sack.string.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestStringDSSackPeekAll extends TestStringDSSackBase {
    public TestStringDSSackPeekAll(StringDSSackFactory factory) {
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
        StringDSSack ds = createDS();
        checkIsEmpty(ds, true);
        checkPeekAllOnEmpty(ds);
    }

    private void testPeekAllOnOne() {
        outln(" - peekAll() on one -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        checkPeekAll(ds, "apple");
    }

    private void testPeekAllOnTwo() {
        outln(" - peekAll() on two -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        checkPeekAll(ds, "apple", "banana");
    }

    private void testPeekAllOnSeveral() {
        outln(" - peekAll() on several -");
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
        checkPeekAll(ds, fruits);
    }
}
