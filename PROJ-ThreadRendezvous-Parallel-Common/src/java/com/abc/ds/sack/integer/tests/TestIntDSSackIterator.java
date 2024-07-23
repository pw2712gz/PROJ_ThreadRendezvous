package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestIntDSSackIterator extends TestIntDSSackBase {
    public TestIntDSSackIterator(IntDSSackFactory factory) {
        super("createIterator()", factory);
    }

    @Override
    protected void performTests() {
        testIteratorOnEmpty();
        testIteratorOnOne();
        testIteratorOnTwo();
        testIteratorOnSeveral();
    }

    private void testIteratorOnEmpty() {
        outln(" - createIterator() on empty -");
        IntDSSack ds = createDS();
        checkIsEmpty(ds, true);
        checkIterator(ds, INT_ZERO_LEN_ARRAY);
    }

    private void testIteratorOnOne() {
        outln(" - createIterator() on one -");
        IntDSSack ds = createDS();
        add(ds, 10);
        checkIterator(ds, 10);
    }

    private void testIteratorOnTwo() {
        outln(" - createIterator() on two -");
        IntDSSack ds = createDS();
        add(ds, 10);
        add(ds, 20);
        checkIterator(ds, 10, 20);
    }

    private void testIteratorOnSeveral() {
        outln(" - createIterator() on several -");
        IntDSSack ds = createDS();
        outln("adding some junk to be cleared before createIterator() test...");
        add(ds, -777);
        add(ds, -888);
        add(ds, -999);
        outln("clear()...");
        ds.clear();

        int[] values = new TestIntGenerator(
            TestIntGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, values);
        checkIterator(ds, values);
    }
}
