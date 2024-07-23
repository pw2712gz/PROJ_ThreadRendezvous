package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSSackContains extends TestDSSackBase {
    public TestDSSackContains(DSSackFactory factory) {
        super("contains()", factory);
    }

    @Override
    protected void performTests() {
        testPeekAllOnEmpty();
        testPeekAllOnOne();
        testPeekAllOnTwo();
        testPeekAllOnSeveral();
    }

    private void testPeekAllOnEmpty() {
        outln(" - contains() on empty -");
        DSSack<String> ds = createDS();
        checkIsEmpty(ds, true);
        checkContains(ds, "mango", false);
    }

    private void testPeekAllOnOne() {
        outln(" - contains() on one -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        checkContains(ds, "apple", true);
        checkContains(ds, "mango", false);
    }

    private void testPeekAllOnTwo() {
        outln(" - contains() on two -");
        DSSack<String> ds = createDS();
        add(ds, "apple");
        add(ds, "banana");
        checkContains(ds, "banana", true);
        checkContains(ds, "mango", false);
    }

    private void testPeekAllOnSeveral() {
        outln(" - contains() on several -");
        DSSack<String> ds = createDS();
        outln("adding some junk to be cleared before peekAll() test...");
        add(ds, "JUNK A");
        add(ds, "JUNK B");
        add(ds, "JUNK C");
        outln("clear()...");
        ds.clear();

        String[] fruits = new TestFruitGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(20);

        add(ds, fruits);

        outln("Expect to find these:");
        checkContains(ds, "banana", true);
        checkContains(ds, "mango", true);
        checkContains(ds, "apple", true);
        checkContains(ds, "cherry", true);
        checkContains(ds, "strawberry", true);
        checkContains(ds, "grape", true);
        checkContains(ds, "fig", true);
        outln("Expect to NOT find these:");
        checkContains(ds, "pear", false);
        checkContains(ds, "lemon", false);
        checkContains(ds, "watermelon", false);
        checkContains(ds, "honeydew", false);
        checkContains(ds, "kiwi", false);
//        for ( String fruit : getFruits(26) ) {
//            checkContains(ds, fruit, true);
//        }
    }
}
