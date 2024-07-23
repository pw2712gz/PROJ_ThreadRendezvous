package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestIntDSSackRemoveAll extends TestIntDSSackBase {
    public TestIntDSSackRemoveAll(IntDSSackFactory factory) {
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
        IntDSSack ds = createDS();
        checkIsEmpty(ds, true);
        checkRemoveAll(ds, INT_ZERO_LEN_ARRAY);
    }

    private void testPeekAllOnOne() {
        outln(" - removeAll() on one -");
        IntDSSack ds = createDS();
        add(ds, 444);
        checkRemoveAll(ds, 444);
        checkCount(ds, 0);
    }

    private void testPeekAllOnTwo() {
        outln(" - removeAll() on two -");
        IntDSSack ds = createDS();
        add(ds, 444);
        add(ds, 555);
        checkCount(ds, 2);
        checkRemoveAll(ds, 444, 555);
        checkCount(ds, 0);
    }

    private void testPeekAllOnSeveral() {
        outln(" - removeAll() on several -");
        IntDSSack ds = createDS();
        outln("adding some junk to be cleared before removeAll() test...");
        add(ds, -777);
        add(ds, -888);
        add(ds, -999);
        outln("clear()...");
        ds.clear();

        int[] items = new TestIntGenerator(
            TestIntGenerator.RANDOM_SEED_5).nextRandom(20);
        add(ds, items);

        int[] expectedItems = unwrap(
            testHelper.potentiallyRemoveDuplicatesAndShuffle(wrap(items)));

        checkCount(ds, expectedItems.length);
        checkRemoveAll(ds, expectedItems);
        checkCount(ds, 0);
    }
}
