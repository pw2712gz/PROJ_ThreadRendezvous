package com.abc.ds.sack.string.tests;

import com.abc.ds.sack.string.*;

/* deliberate package access */
class TestStringDSSackClear extends TestStringDSSackBase {
    public TestStringDSSackClear(StringDSSackFactory factory) {
        super("clear tests", factory);
    }

    @Override
    protected void performTests() {
        testAddThenClear();
        testAddThenClearThenAdd();
    }

    private void testAddThenClear() {
        outln(" - add, then clear -");
        StringDSSack ds = createDS();
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
        StringDSSack ds = createDS();
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
