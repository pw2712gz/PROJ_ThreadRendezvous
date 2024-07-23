package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;

/* deliberate package access */
class TestDSSackClear extends TestDSSackBase {
    public TestDSSackClear(DSSackFactory factory) {
        super("clear tests", factory);
    }

    @Override
    protected void performTests() {
        testAddThenClear();
        testAddThenClearThenAdd();
    }

    private void testAddThenClear() {
        outln(" - add, then clear -");
        DSSack<String> ds = createDS();
        outln("adding 5:");
        add(ds, getFruits(5));
        checkCount(ds, 5);

        outln("clear()");
        ds.clear();
        checkIsEmpty(ds, true);
        checkCount(ds, 0);
    }

    private void testAddThenClearThenAdd() {
        outln(" - add, then clear, then add -");
        DSSack<String> ds = createDS();
        outln("adding 5:");
        add(ds, getFruits(5));
        checkCount(ds, 5);

        outln("clear()");
        ds.clear();
        checkCount(ds, 0);

        outln("adding 3:");
        add(ds, getFruits(5, 3));
        checkIsEmpty(ds, false);
        checkCount(ds, 3);
    }
}
