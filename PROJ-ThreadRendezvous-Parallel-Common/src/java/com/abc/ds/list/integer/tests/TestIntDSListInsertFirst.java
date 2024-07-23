package com.abc.ds.list.integer.tests;

import com.abc.ds.list.integer.*;

/* deliberate package access */
class TestIntDSListInsertFirst extends TestIntDSListBase {
    public TestIntDSListInsertFirst(IntDSListFactory factory) {
        super("insertFirst()", factory);
    }

    @Override
    protected void performTests() {
        testInsertFirstWithThreeAlready();
        testInsertFirstOnEmpty();
        testInsertFirstSeveral();
    }

    private void testInsertFirstWithThreeAlready() {
        outln(" - insertFirst() with three already -");
        IntDSList ds = createDS();
        add(ds, 10, 20, 30);
        outln("insertFirst(5)...");
        ds.insertFirst(5);
        checkPeekAll(ds, 5, 10, 20, 30);
    }

    private void testInsertFirstOnEmpty() {
        outln(" - insertFirst() on empty -");
        IntDSList ds = createDS();
        outln("isEmpty()", ds.isEmpty(), true);
        outln("insertFirst(200)...");
        ds.insertFirst(200);
        checkPeekAll(ds, 200);
    }

    private void testInsertFirstSeveral() {
        outln(" - insertFirst() several times -");
        IntDSList ds = createDS();
        outln("insertFirst(100)...");
        ds.insertFirst(100);
        outln("insertFirst(200)...");
        ds.insertFirst(200);
        outln("insertFirst(300)...");
        ds.insertFirst(300);
        checkPeekAll(ds, 300, 200, 100);
    }
}
