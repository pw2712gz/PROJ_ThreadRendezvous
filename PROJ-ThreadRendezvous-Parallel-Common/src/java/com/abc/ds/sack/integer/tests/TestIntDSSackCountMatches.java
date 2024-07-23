package com.abc.ds.sack.integer.tests;

import com.abc.ds.filter.integer.*;
import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestIntDSSackCountMatches extends TestIntDSSackBase {
    public TestIntDSSackCountMatches(IntDSSackFactory factory) {
        super("countMatches(filter)", factory);
    }

    @Override
    protected void performTests() {
        testCountMatchesOnEmpty();
        testCountMatchesOnOne();
        testCountMatchesOnTwo();
        testCountMatchesOnSeveral();
    }

    private void testCountMatchesOnEmpty() {
        outln(" - countMatches(filter) on empty -");
        IntDSSack ds = createDS();
        checkIsEmpty(ds, true);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), 0);
    }

    private void testCountMatchesOnOne() {
        outln(" - countMatches() on one -");
        IntDSSack ds = createDS();
        add(ds, 50);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), 1);
    }

    private void testCountMatchesOnTwo() {
        outln(" - countMatches() on two -");
        IntDSSack ds = createDS();
        add(ds, 50);
        add(ds, 125);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), 1);
    }

    private void testCountMatchesOnSeveral() {
        outln(" - countMatches() on several -");
        IntDSSack ds = createDS();

        int[] items = new TestIntGenerator(
            TestIntGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, items);

        int targetLessThanValue = 250;

        int expectedMatchCount = 0;
        for ( int item : items ) {
            if ( item < targetLessThanValue ) {
                expectedMatchCount++;
            }
        }

        IntDSFilter filter = createFilterLessThan(targetLessThanValue);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), expectedMatchCount);
    }
}
