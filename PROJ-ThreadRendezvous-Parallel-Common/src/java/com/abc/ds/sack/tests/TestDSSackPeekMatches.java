package com.abc.ds.sack.tests;

import java.util.*;

import com.abc.ds.filter.*;
import com.abc.ds.sack.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestDSSackPeekMatches extends TestDSSackBase {
    public TestDSSackPeekMatches(DSSackFactory factory) {
        super("peekMatches(filter)", factory);
    }

    @Override
    protected void performTests() {
        testPeekMatchesOnEmpty();
        testPeekMatchesOnOne();
        testPeekMatchesOnTwo();
        testPeekMatchesOnSeveral();
    }

    private void testPeekMatchesOnEmpty() {
        outln(" - peekMatches(filter) on empty -");
        DSSack<String> ds = createDS();
        checkIsEmpty(ds, true);
        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, StringTools.ZERO_LEN_ARRAY);
    }

    private void testPeekMatchesOnOne() {
        outln(" - peekMatches() on one -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, "apple");
    }

    private void testPeekMatchesOnTwo() {
        outln(" - peekMatches() on two -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, "apple");
    }

    private void testPeekMatchesOnSeveral() {
        outln(" - peekMatches() on several -");
        DSSack<String> ds = createDS();

        String[] fruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, fruits);

        int targetStringLen = 6;

        List<String> expectedList = new ArrayList<>();
        for ( String fruit : fruits ) {
            if ( fruit.length() == targetStringLen ) {
                expectedList.add(fruit);
            }
        }

        DSFilter<String> filter = createStringLengthFilter(targetStringLen);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, StringTools.toArray(expectedList));
    }
}
