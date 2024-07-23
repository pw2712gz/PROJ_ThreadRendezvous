package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;

/* deliberate package access */
class TestIntDSSackClear extends TestIntDSSackBase {
    public TestIntDSSackClear(IntDSSackFactory factory) {
        super("clear tests", factory);
    }

    @Override
    protected void performTests() {
        testAddThenClear();
        testAddThenClearThenAdd();
    }

    private void testAddThenClear() {
        outln(" - add, then clear -");
        IntDSSack ds = createDS();
        outln("adding 5:");
        add(ds, getInts(5));
        checkCount(ds, 5);

        outln("clear()");
        ds.clear();
        checkIsEmpty(ds, true);
        checkCount(ds, 0);
    }

    private void testAddThenClearThenAdd() {
        outln(" - add, then clear, then add -");
        IntDSSack ds = createDS();
        outln("adding 5:");
        add(ds, getInts(5));
        checkCount(ds, 5);

        outln("clear()");
        ds.clear();
        checkCount(ds, 0);

        outln("adding 3:");
        add(ds, getInts(5, 3));
        checkIsEmpty(ds, false);
        checkCount(ds, 3);
    }
}
