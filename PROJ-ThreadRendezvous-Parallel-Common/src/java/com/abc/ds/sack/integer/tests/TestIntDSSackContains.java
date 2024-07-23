package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestIntDSSackContains extends TestIntDSSackBase {
    public TestIntDSSackContains(IntDSSackFactory factory) {
        super("contains()", factory);
    }

    @Override
    protected void performTests() {
        testPeekAllOnEmpty();
        testPeekAllOnOne();
        testPeekAllOnTwo();
        testPeekAllOnSeveral();
    }

    private void testPeekAllOnEmpty() {
        outln(" - contains() on empty -");
        IntDSSack ds = createDS();
        checkIsEmpty(ds, true);
        checkContains(ds, 999, false);
    }

    private void testPeekAllOnOne() {
        outln(" - contains() on one -");
        IntDSSack ds = createDS();
        add(ds, 10);
        checkContains(ds, 10, true);
        checkContains(ds, 999, false);
    }

    private void testPeekAllOnTwo() {
        outln(" - contains() on two -");
        IntDSSack ds = createDS();
        add(ds, 10);
        add(ds, 20);
        checkContains(ds, 10, true);
        checkContains(ds, 20, true);
        checkContains(ds, 999, false);
    }

    private void testPeekAllOnSeveral() {
        outln(" - contains() on several -");
        IntDSSack ds = createDS();
        outln("adding some junk to be cleared before peekAll() test...");
        add(ds, -777);
        add(ds, -888);
        add(ds, -999);
        outln("clear()...");
        ds.clear();

        int[] ints = new TestIntGenerator(
            TestIntGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, ints);

        outln("Expect to find these:");
        checkContains(ds, 120, true);
        checkContains(ds, 130, true);
        checkContains(ds, 140, true);
        checkContains(ds, 160, true);
        checkContains(ds, 170, true);
        checkContains(ds, 230, true);
        checkContains(ds, 270, true);

        outln("Expect to NOT find these:");
        checkContains(ds, 150, false);
        checkContains(ds, 180, false);
        checkContains(ds, 190, false);
        checkContains(ds, 200, false);
        checkContains(ds, 210, false);
        checkContains(ds, 250, false);
        checkContains(ds, -777, false);

//        for ( int value : getInts(26) ) {
//            checkContains(ds, value, true);
//        }
    }
}
