package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestIntDSSackPeekAll extends TestIntDSSackBase {
    public TestIntDSSackPeekAll(IntDSSackFactory factory) {
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
        IntDSSack ds = createDS();
        checkIsEmpty(ds, true);
        checkPeekAllOnEmpty(ds);
    }

    private void testPeekAllOnOne() {
        outln(" - peekAll() on one -");
        IntDSSack ds = createDS();
        add(ds, 10);
        checkPeekAll(ds, 10);
    }

    private void testPeekAllOnTwo() {
        outln(" - peekAll() on two -");
        IntDSSack ds = createDS();
        add(ds, 10);
        add(ds, 20);
        checkPeekAll(ds, 10, 20);
    }

    private void testPeekAllOnSeveral() {
        outln(" - peekAll() on several -");
        IntDSSack ds = createDS();
        outln("adding some junk to be cleared before peekAll() test...");
        add(ds, -777);
        add(ds, -888);
        add(ds, -999);
        outln("clear()...");
        ds.clear();

        int[] values = new TestIntGenerator(
            TestIntGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, values);
        checkPeekAll(ds, values);
    }
}
