package com.abc.ds.sack.tests;

import java.util.*;

import com.abc.ds.filter.*;
import com.abc.ds.sack.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSackRemoveAndReturnMatches extends TestDSSackBase {
    public TestDSSackRemoveAndReturnMatches(DSSackFactory factory) {
        super("removeAndReturnMatches(filter)", factory);
    }

    @Override
    protected void performTests() {
        testRemoveMatchesOnEmpty();
        testRemoveMatchesOnOne();
        testRemoveMatchesOnTwo();
        testRemoveMatchesOnSeveral();
    }

    private void testRemoveMatchesOnEmpty() {
        outln(" - removeAndReturnMatches(filter) on empty -");
        DSSack<String> ds = createDS();
        checkIsEmpty(ds, true);
        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        outln("removeAndReturnMatches(filter)", ds.removeAndReturnMatches(filter), StringTools.ZERO_LEN_ARRAY );
    }

    private void testRemoveMatchesOnOne() {
        outln(" - removeAndReturnMatches() on one -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        outln("removeAndReturnMatches(filter)",
            ds.removeAndReturnMatches(filter),
            new String[] { "apple" });
        checkCount(ds, 0);
    }

    private void testRemoveMatchesOnTwo() {
        outln(" - removeAndReturnMatches() on two -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        outln("removeAndReturnMatches(filter)",
            ds.removeAndReturnMatches(filter),
            new String[] { "apple" });
        checkCount(ds, 1);
    }

    private void testRemoveMatchesOnSeveral() {
        outln(" - removeAndReturnMatches() on several -");
        DSSack<String> ds = createDS();

        String[] fruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, fruits);

        int targetStringLen = 5;

        String[] unfilteredExpected =
            testHelper.potentiallyRemoveDuplicatesAndShuffle(fruits);
        List<String> expectedRemoveList = new ArrayList<>();
        List<String> expectedKeepList = new ArrayList<>();
        for ( String fruit : unfilteredExpected ) {
            if ( fruit.length() == targetStringLen ) {
                expectedRemoveList.add(fruit);
            } else {
                expectedKeepList.add(fruit);
            }
        }
        String[] filteredExpected = StringTools.toArray(expectedRemoveList);
        String[] expectedKeep = StringTools.toArray(expectedKeepList);
        int expectedCountBeforeRemoval = unfilteredExpected.length;
        //int expectedRemoveCount = filteredExpected.length;
        int expectedCountAfterRemoval = expectedKeep.length;

        checkCount(ds, expectedCountBeforeRemoval);

        DSFilter<String> filter = createStringLengthFilter(targetStringLen);
        outln("filtering with: " + filter);

        testHelper.check(
            "removeAndReturnMatches(filter)",
            ds.removeAndReturnMatches(filter),
            filteredExpected);
        checkCount(ds, expectedCountAfterRemoval);
        checkPeekAll(ds, expectedKeep);
    }
}
