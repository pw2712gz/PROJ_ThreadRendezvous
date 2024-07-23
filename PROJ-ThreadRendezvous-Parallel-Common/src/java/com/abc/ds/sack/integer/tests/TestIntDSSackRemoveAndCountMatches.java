package com.abc.ds.sack.integer.tests;

import com.abc.ds.filter.integer.*;
import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestIntDSSackRemoveAndCountMatches extends TestIntDSSackBase {
    public TestIntDSSackRemoveAndCountMatches(IntDSSackFactory factory) {
        super("removeAndCountMatches(filter)", factory);
    }

    @Override
    protected void performTests() {
        testremoveAndCountMatchesOnEmpty();
        testremoveAndCountMatchesOnOne();
        testremoveAndCountMatchesOnTwo();
        testremoveAndCountMatchesOnSeveral();
    }

    private void testremoveAndCountMatchesOnEmpty() {
        outln(" - removeAndCountMatches(filter) on empty -");
        IntDSSack ds = createDS();
        checkIsEmpty(ds, true);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        outln("removeAndCountMatches(filter)", ds.removeAndCountMatches(filter), 0);
    }

    private void testremoveAndCountMatchesOnOne() {
        outln(" - removeAndCountMatches() on one -");
        IntDSSack ds = createDS();
        add(ds, 50);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        outln("removeAndCountMatches(filter)", ds.removeAndCountMatches(filter), 1);
    }

    private void testremoveAndCountMatchesOnTwo() {
        outln(" - removeAndCountMatches() on two -");
        IntDSSack ds = createDS();
        add(ds, 50);
        add(ds, 125);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        outln("removeAndCountMatches(filter)", ds.removeAndCountMatches(filter), 1);
    }

    private void testremoveAndCountMatchesOnSeveral() {
        outln(" - removeAndCountMatches() on several -");
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
        outln("removeAndCountMatches(filter)", ds.removeAndCountMatches(filter), expectedMatchCount);
    }
}
