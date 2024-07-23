package com.abc.ds.sack.string.tests;

import java.util.*;

import com.abc.ds.filter.string.*;
import com.abc.ds.sack.string.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestStringDSSackRemoveAndCountMatches extends TestStringDSSackBase {
    public TestStringDSSackRemoveAndCountMatches(StringDSSackFactory factory) {
        super("removeAndCountMatches(filter)", factory);
    }

    @Override
    protected void performTests() {
        testRemoveMatchesOnEmpty();
        testRemoveMatchesOnOne();
        testRemoveMatchesOnTwo();
        testRemoveMatchesOnSeveral();
    }

    private void testRemoveMatchesOnEmpty() {
        outln(" - removeAndCountMatches(filter) on empty -");
        StringDSSack ds = createDS();
        checkIsEmpty(ds, true);
        StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);
        outln("removeAndCountMatches(filter)", ds.removeAndCountMatches(filter), 0);
    }

    private void testRemoveMatchesOnOne() {
        outln(" - removeAndCountMatches() on one -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);
        outln("removeAndCountMatches(filter)", ds.removeAndCountMatches(filter), 1);
        checkCount(ds, 0);
    }

    private void testRemoveMatchesOnTwo() {
        outln(" - removeAndCountMatches() on two -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);
        outln("removeAndCountMatches(filter)", ds.removeAndCountMatches(filter), 1);
        checkCount(ds, 1);
    }

    private void testRemoveMatchesOnSeveral() {
        outln(" - removeAndCountMatches() on several -");
        StringDSSack ds = createDS();

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
        int expectedRemoveCount = filteredExpected.length;
        int expectedCountAfterRemoval = expectedKeep.length;

        checkCount(ds, expectedCountBeforeRemoval);

        StringDSFilter filter = createMatchLengthStringDSFilter(targetStringLen);
        outln("filtering with: " + filter);
        outln("removeAndCountMatches(filter)", ds.removeAndCountMatches(filter), expectedRemoveCount);
        checkCount(ds, expectedCountAfterRemoval);
        checkPeekAll(ds, expectedKeep);


//
//        String[] expectedFruits = potentiallyRemoveDuplicatesAndShuffle(fruits);
//
//        checkCount(ds, expectedFruits.length);
//        checkRemoveAll(ds, expectedFruits);
//        checkCount(ds, 0);

    }
}
