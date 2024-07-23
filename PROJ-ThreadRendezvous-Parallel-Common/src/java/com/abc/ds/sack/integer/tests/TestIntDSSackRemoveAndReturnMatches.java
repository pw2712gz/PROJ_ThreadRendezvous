package com.abc.ds.sack.integer.tests;

import java.util.*;

import com.abc.ds.filter.integer.*;
import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestIntDSSackRemoveAndReturnMatches extends TestIntDSSackBase {
    public TestIntDSSackRemoveAndReturnMatches(IntDSSackFactory factory) {
        super("removeAndReturnMatches(filter)", factory);
    }

    @Override
    protected void performTests() {
        testRemoveAndReturnMatchesOnEmpty();
        testRemoveAndReturnMatchesOnOneAndMatch();
        testRemoveAndReturnMatchesOnOneAndMiss();
        testRemoveAndReturnMatchesOnTwo();
        testRemoveAndReturnMatchesOnSeveral();
    }

    private void testRemoveAndReturnMatchesOnEmpty() {
        outln(" - removeAndReturnMatches(filter) on empty -");
        IntDSSack ds = createDS();
        checkIsEmpty(ds, true);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        outln("removeAndReturnMatches(filter)",
            ds.removeAndReturnMatches(filter), INT_ZERO_LEN_ARRAY);
    }

    private void testRemoveAndReturnMatchesOnOneAndMatch() {
        outln(" - removeAndReturnMatches() on one and match -");
        IntDSSack ds = createDS();
        add(ds, 50);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        outln("removeAndReturnMatches(filter)",
            ds.removeAndReturnMatches(filter), new int[] { 50 });
    }

    private void testRemoveAndReturnMatchesOnOneAndMiss() {
        outln(" - removeAndReturnMatches() on one and miss-");
        IntDSSack ds = createDS();
        add(ds, 50);
        IntDSFilter filter = createFilterLessThan(25);
        outln("filtering with: " + filter);
        outln("removeAndReturnMatches(filter)",
            ds.removeAndReturnMatches(filter), INT_ZERO_LEN_ARRAY);
    }

    private void testRemoveAndReturnMatchesOnTwo() {
        outln(" - removeAndReturnMatches() on two matching just one -");
        IntDSSack ds = createDS();
        add(ds, 30);
        add(ds, 20);
        IntDSFilter filter = createFilterLessThan(25);
        outln("filtering with: " + filter);
        outln("removeAndReturnMatches(filter)",
            ds.removeAndReturnMatches(filter), new int[] { 20 });
    }

    private void testRemoveAndReturnMatchesOnSeveral() {
        outln(" - removeAndReturnMatches() on several -");
        IntDSSack ds = createDS();

        int[] items = new TestIntGenerator(
            TestIntGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, items);

        int targetLessThanValue = 250;

        List<Integer> expectedList = new ArrayList<>();
        for ( int item : items ) {
            if ( item < targetLessThanValue ) {
                expectedList.add(item);
            }
        }

        IntDSFilter filter = createFilterLessThan(targetLessThanValue);
        outln("filtering with: " + filter);
        outln("removeAndReturnMatches(filter)",
            ds.removeAndReturnMatches(filter), unwrap(expectedList));
    }
}
