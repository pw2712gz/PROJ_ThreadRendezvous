package com.abc.ds.sack.string.tests;

import com.abc.ds.filter.string.*;
import com.abc.ds.sack.string.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestStringDSSackCountMatches extends TestStringDSSackBase {
    public TestStringDSSackCountMatches(StringDSSackFactory factory) {
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
        StringDSSack ds = createDS();
        checkIsEmpty(ds, true);
        StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), 0);
    }

    private void testCountMatchesOnOne() {
        outln(" - countMatches() on one -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), 1);
    }

    private void testCountMatchesOnTwo() {
        outln(" - countMatches() on two -");
        StringDSSack ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        StringDSFilter filter = createMatchLengthStringDSFilter(5);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), 1);
    }

    private void testCountMatchesOnSeveral() {
        outln(" - countMatches() on several -");
        StringDSSack ds = createDS();

        String[] fruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, fruits);

        int targetStringLen = 5;

        int expectedMatchCount = 0;
        String[] unfilteredExpected =
            testHelper.potentiallyRemoveDuplicatesAndShuffle(fruits);
        for ( String fruit : unfilteredExpected ) {
            if ( fruit.length() == targetStringLen ) {
                expectedMatchCount++;
            }
        }

        StringDSFilter filter = createMatchLengthStringDSFilter(targetStringLen);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), expectedMatchCount);
    }
}
