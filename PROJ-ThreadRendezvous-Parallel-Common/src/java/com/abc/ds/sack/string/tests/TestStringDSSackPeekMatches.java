package com.abc.ds.sack.string.tests;

import java.util.*;

import com.abc.ds.filter.string.*;
import com.abc.ds.sack.string.*;
import com.abc.ds.tests.*;
import com.programix.util.*;

/* deliberate package access */
class TestStringDSSackPeekMatches extends TestStringDSSackBase {
    public TestStringDSSackPeekMatches(StringDSSackFactory factory) {
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
        StringDSSack ds = createDS();
        checkIsEmpty(ds, true);
        StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, StringTools.ZERO_LEN_ARRAY);
    }

    private void testPeekMatchesOnOne() {
        outln(" - peekMatches() on one -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, "apple");
    }

    private void testPeekMatchesOnTwo() {
        outln(" - peekMatches() on two -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, "apple");
    }

    private void testPeekMatchesOnSeveral() {
        outln(" - peekMatches() on several -");
        StringDSSack ds = createDS();

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

        StringDSFilter filter = createMatchLengthStringDSFilter(targetStringLen);
        outln("filtering with: " + filter);
        checkPeekMatches(ds, filter, StringTools.toArray(expectedList));
    }
}
