package com.abc.ds.list.tests;

import com.abc.ds.list.*;

/* deliberate package access */
class TestDSListInsertBefore extends TestDSListBase {
    public TestDSListInsertBefore(DSListFactory factory) {
        super("insertBefore()", factory);
    }

    @Override
    protected void performTests() {
        testInsertBeforeInTheMiddle();
        testInsertBeforeTheLastOne();
        testInsertBeforeTheFirstOne();
        testInsertBeforeAtTheEnd();
        testInsertBeforeNegativeIndex();
        testInsertBeforeBarelyTooLargeIndex();
        testInsertBeforeWayTooLargeIndex();
    }

    private void testInsertBeforeInTheMiddle() {
        outln(" - insertBefore() a middle item -");
        DSList<String> ds = createDS();
        add(ds, "apple", "banana", "cherry");
        outln("insertBefore(1, \"grape\")...");
        ds.insertBefore(1, "grape");
        checkPeekAll(ds, "apple", "grape", "banana", "cherry");
    }

    private void testInsertBeforeTheLastOne() {
        outln(" - insertBefore() the last one -");
        DSList<String> ds = createDS();
        add(ds, "apple", "banana", "cherry");
        outln("insertBefore(2, \"grape\")...");
        ds.insertBefore(2, "grape");
        checkPeekAll(ds, "apple", "banana", "grape", "cherry");
    }

    private void testInsertBeforeTheFirstOne() {
        outln(" - insertBefore() the first one -");
        DSList<String> ds = createDS();
        add(ds, "apple", "banana", "cherry");
        outln("insertBefore(0, \"grape\")...");
        ds.insertBefore(0, "grape");
        checkPeekAll(ds, "grape", "apple", "banana", "cherry");
    }

    private void testInsertBeforeAtTheEnd() {
        outln(" - insertBefore() at the end (append to the end) -");
        DSList<String> ds = createDS();
        add(ds, "apple", "banana", "cherry");
        outln("insertBefore(count(), \"grape\")...");
        ds.insertBefore(ds.count(), "grape");
        checkPeekAll(ds, "apple", "banana", "cherry", "grape");
    }

    private void testInsertBeforeNegativeIndex() {
        outln(" - insertBefore() negative index [expect exception] -");
        DSList<String> ds = createDS();
        add(ds, "apple", "banana", "cherry");
        insertBeforeBadIndex(ds, -1, "grape");
    }

    private void testInsertBeforeBarelyTooLargeIndex() {
        outln(" - insertBefore() barely too large index [expect exception] -");
        DSList<String> ds = createDS();
        add(ds, "apple", "banana", "cherry");
        insertBeforeBadIndex(ds, ds.count() + 1, "grape");
    }

    private void testInsertBeforeWayTooLargeIndex() {
        outln(" - insertBefore() way too large index [expect exception] -");
        DSList<String> ds = createDS();
        add(ds, "apple", "banana", "cherry");
        insertBeforeBadIndex(ds, 100, "grape");
    }
}
