package com.abc.ds.sack.integer.tests;

import java.util.*;

import com.abc.ds.filter.integer.*;
import com.abc.ds.sack.integer.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestIntDSSackPeekMatches extends TestIntDSSackBase {
    public TestIntDSSackPeekMatches(IntDSSackFactory factory) {
        super("peekMatches(filter)", factory);
    }

    @Override
    protected void performTests() {
        testPeekMatchesOnEmpty();
        testPeekMatchesOnOneAndMatch();
        testPeekMatchesOnOneAndMiss();
        testPeekMatchesOnTwo();
        testPeekMatchesOnSeveral();
    }

    private void testPeekMatchesOnEmpty() {
        outln(" - peekMatches(filter) on empty -");
        IntDSSack ds = createDS();
        checkIsEmpty(ds, true);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, INT_ZERO_LEN_ARRAY);
    }

    private void testPeekMatchesOnOneAndMatch() {
        outln(" - peekMatches() on one and match -");
        IntDSSack ds = createDS();
        add(ds, 50);
        IntDSFilter filter = createFilterLessThan(100);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, 50);
    }

    private void testPeekMatchesOnOneAndMiss() {
        outln(" - peekMatches() on one and miss-");
        IntDSSack ds = createDS();
        add(ds, 50);
        IntDSFilter filter = createFilterLessThan(25);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, INT_ZERO_LEN_ARRAY);
    }

    private void testPeekMatchesOnTwo() {
        outln(" - peekMatches() on two matching just one -");
        IntDSSack ds = createDS();
        add(ds, 30);
        add(ds, 20);
        IntDSFilter filter = createFilterLessThan(25);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, 20);
    }

    private void testPeekMatchesOnSeveral() {
        outln(" - peekMatches() on several -");
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
        checkPeekMatches(ds, filter, unwrap(expectedList));
    }
}
