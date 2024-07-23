package com.abc.ds.sack.string.tests;

import com.abc.ds.sack.string.*;

/* deliberate package access */
class TestStringDSSackBasicEmptyCount extends TestStringDSSackBase {
    public TestStringDSSackBasicEmptyCount(StringDSSackFactory factory) {
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
        StringDSSack ds = createDS();
        outln("initial state: ");
        checkIsEmpty(ds, true);
        checkCount(ds, 0);
    }

    private void testAddJustOne() {
        outln(" - add just one -");
        StringDSSack ds = createDS();
        addExpectGood(ds, "apple");
        checkIsEmpty(ds, false);
        checkCount(ds, 1);
    }

    private void testAddTwo() {
        outln(" - add two -");
        StringDSSack ds = createDS();
        addExpectGood(ds, "apple", "banana");
        checkIsEmpty(ds, false);
        checkCount(ds, 2);
    }

    private void testAddFive() {
        outln(" - add five -");
        StringDSSack ds = createDS();
        addExpectGood(ds, "apple", "banana", "cherry", "date", "eggplant");
        checkIsEmpty(ds, false);
        checkCount(ds, 5);
    }

    /*
    private void testAddPeek() {
        outln(" - add and peek -");
        StringDSSack sack = createDSSack();
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
