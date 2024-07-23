package com.abc.ds.sack.tests;

import com.abc.ds.sack.*;

/* deliberate package access */
class TestDSSackBasicEmptyCount extends TestDSSackBase {
    public TestDSSackBasicEmptyCount(DSSackFactory factory) {
        super("empty and count tests", factory);
    }

    @Override
    protected void performTests() {
        testInitialState();
        testAddJustOne();
        testAddTwo();
        testAddFive();
        //testAddPeek();
    }

    private void testInitialState() {
        DSSack<String> ds = createDS();
        outln("initial state: ");
        checkIsEmpty(ds, true);
        checkCount(ds, 0);
    }

    private void testAddJustOne() {
        outln(" - add just one -");
        DSSack<String> ds = createDS();
        addExpectGood(ds, "apple");
        checkIsEmpty(ds, false);
        checkCount(ds, 1);
    }

    private void testAddTwo() {
        outln(" - add two -");
        DSSack<String> ds = createDS();
        addExpectGood(ds, "apple", "banana");
        checkIsEmpty(ds, false);
        checkCount(ds, 2);
    }

    private void testAddFive() {
        outln(" - add five -");
        DSSack<String> ds = createDS();
        addExpectGood(ds, "apple", "banana", "cherry", "date", "eggplant");
        checkIsEmpty(ds, false);
        checkCount(ds, 5);
    }

    /*
    private void testAddPeek() {
        outln(" - add and peek -");
        DSSack<String> sack = createDSSack();
        String[] fruitsAdded = new String[] {
            "a", "c", "d", "a", "a", "a", "a", "a", "m"
        };
        String[] expectedItems = new String[] {
            "c", "a", "b", "c", "b", "a", "x", "y", "x"
        };

        add(sack, fruitsAdded);
        outln("checking count()", sack.count(), fruitsAdded.length);

        checkPeekAll(sack, expectedItems);
    }
     */
}
