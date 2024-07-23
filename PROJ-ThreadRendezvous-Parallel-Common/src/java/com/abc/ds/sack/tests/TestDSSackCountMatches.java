package com.abc.ds.sack.tests;

import com.abc.ds.filter.*;
import com.abc.ds.sack.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSSackCountMatches extends TestDSSackBase {
    public TestDSSackCountMatches(DSSackFactory factory) {
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
        DSSack<String> ds = createDS();
        checkIsEmpty(ds, true);
        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), 0);
    }

    private void testCountMatchesOnOne() {
        outln(" - countMatches() on one -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), 1);
    }

    private void testCountMatchesOnTwo() {
        outln(" - countMatches() on two -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        DSFilter<String> filter = createStringLengthFilter(5);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), 1);
    }

    private void testCountMatchesOnSeveral() {
        outln(" - countMatches() on several -");
        DSSack<String> ds = createDS();

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

        DSFilter<String> filter = createStringLengthFilter(targetStringLen);
        outln("filtering with: " + filter);
        outln("countMatches(filter)", ds.countMatches(filter), expectedMatchCount);
    }
}
