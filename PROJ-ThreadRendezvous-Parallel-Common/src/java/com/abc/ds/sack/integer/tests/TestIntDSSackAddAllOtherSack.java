package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;

/* deliberate package access */
class TestIntDSSackAddAllOtherSack extends TestIntDSSackBase {
    public TestIntDSSackAddAllOtherSack(IntDSSackFactory factory) {
        super("addAll(IntDSSack otherSack)", factory);
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

        IntDSSack otherSack = createAltIntSack();
        otherSack.add(10);
        outln("otherSack contents: " + formatCommaDelimited(otherSack.peekAll()));

        IntDSSack ds = createDS();
        outln("addAll(otherSack)");
        ds.addAll(otherSack);
        checkPeekAll(ds, 10);
    }

    private void testAddAllWithTwo() {
        outln(" - addAll() with two -");
        IntDSSack otherSack = createAltIntSack();
        otherSack.add(500);
        otherSack.add(600);
        outln("otherSack contents: " + formatCommaDelimited(otherSack.peekAll()));

        IntDSSack ds = createDS();
        outln("addAll(otherSack)");
        ds.addAll(otherSack);
        checkPeekAll(ds, 500, 600);
    }

    private void testaddAllWithSeveral() {
        outln(" - addAll() with several -");

        int[] items = getInts(26);
        IntDSSack otherSack = createAltIntSack();
        otherSack.addAll(items);
        outln("otherSack contents: " + formatCommaDelimited(otherSack.peekAll()));

        IntDSSack ds = createDS();
        outln("addAll(otherSack)");
        ds.addAll(otherSack);
        checkPeekAll(ds, items);
    }

    private void testAddAllWithNone() {
        outln(" - addAll() with zero-length array -");
        IntDSSack ds = createDS();
        IntDSSack otherSack = createAltIntSack();
        outln("otherSack.count()", otherSack.count(), 0);
        outln("addAll(otherSack)");
        ds.addAll(otherSack);
        checkIsEmpty(ds, true);
        checkPeekAll(ds, INT_ZERO_LEN_ARRAY);
    }

    private void testAddAllWithNull() {
        outln(" - addAll() with null -");
        IntDSSack ds = createDS();
        IntDSSack otherSack = null;
        outln("otherSack == null", otherSack == null);
        outln("addAll(otherSack)  // otherSack is null, should quietly do nothing");
        ds.addAll(otherSack);
        checkIsEmpty(ds, true);
        checkPeekAll(ds, INT_ZERO_LEN_ARRAY);
    }
}
