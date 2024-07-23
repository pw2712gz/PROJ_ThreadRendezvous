package com.abc.ds.sack.integer.tests;

import com.abc.ds.sack.integer.*;

/* deliberate package access */
class TestIntDSSackBasicEmptyCount extends TestIntDSSackBase {
    public TestIntDSSackBasicEmptyCount(IntDSSackFactory factory) {
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
        IntDSSack ds = createDS();
        outln("initial state: ");
        checkIsEmpty(ds, true);
        checkCount(ds, 0);
    }

    private void testAddJustOne() {
        outln(" - add just one -");
        IntDSSack ds = createDS();
        addExpectGood(ds, 10);
        checkIsEmpty(ds, false);
        checkCount(ds, 1);
    }

    private void testAddTwo() {
        outln(" - add two -");
        IntDSSack ds = createDS();
        addExpectGood(ds, 10, 20);
        checkIsEmpty(ds, false);
        checkCount(ds, 2);
    }

    private void testAddFive() {
        outln(" - add five -");
        IntDSSack ds = createDS();
        addExpectGood(ds, 10, 20, 30, 40, 50);
        checkIsEmpty(ds, false);
        checkCount(ds, 5);
    }

    /*
    private void testAddPeek() {
        outln(" - add and peek -");
        IntDSSack sack = createDSSack();
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
