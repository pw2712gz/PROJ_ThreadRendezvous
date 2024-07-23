package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;

/* deliberate package access */
class TestIntDSSackAddAll extends TestIntDSSackBase {
    public TestIntDSSackAddAll(IntDSSackFactory factory) {
        super("addAll()", factory);
    }

    @Override
    protected void performTests() {
        testAddAllWithOne();
        testAddAllWithTwo();
        testaddAllWithSeveral();
        testAddAllWithNone();
        testAddAllWithNull();
    }

    private void testAddAllWithOne() {
        outln(" - addAll() with one -");
        IntDSSack ds = createDS();
        outln("addAll(10)");
        ds.addAll(10);
        checkPeekAll(ds, 10);
    }

    private void testAddAllWithTwo() {
        outln(" - addAll() with two -");
        IntDSSack ds = createDS();
        outln("addAll(500, 600)");
        ds.addAll(500, 600);
        checkPeekAll(ds, 500, 600);
    }

    private void testaddAllWithSeveral() {
        outln(" - addAll() with several -");
        IntDSSack ds = createDS();
        int[] items = getInts(26);
        outln("addAll(" + formatCommaDelimited(items) + ")");
        ds.addAll(items);
        checkPeekAll(ds, items);
    }

    private void testAddAllWithNone() {
        outln(" - addAll() with zero-length array -");
        IntDSSack ds = createDS();
        outln("addAll(new int[0])");
        ds.addAll(new int[0]);
        checkIsEmpty(ds, true);
        checkPeekAll(ds, INT_ZERO_LEN_ARRAY);
    }

    private void testAddAllWithNull() {
        outln(" - addAll() null -");
        IntDSSack ds = createDS();
        int[] items = null;
        outln("items == null", items == null);
        outln("addAll(items)  // items is null, should quietly do nothing");
        ds.addAll(items);
        checkIsEmpty(ds, true);
        checkPeekAll(ds, INT_ZERO_LEN_ARRAY);
    }
}
