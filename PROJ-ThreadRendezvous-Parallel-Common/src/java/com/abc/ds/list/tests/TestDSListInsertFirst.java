package com.abc.ds.list.tests;

import com.abc.ds.list.*;

/* deliberate package access */
class TestDSListInsertFirst extends TestDSListBase {
    public TestDSListInsertFirst(DSListFactory factory) {
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
        DSList<String> ds = createDS();
        add(ds, "apple", "banana", "cherry");
        outln("insertFirst(\"grape\")...");
        ds.insertFirst("grape");
        checkPeekAll(ds, "grape", "apple", "banana", "cherry");
    }

    private void testInsertFirstOnEmpty() {
        outln(" - insertFirst() on empty -");
        DSList<String> ds = createDS();
        outln("isEmpty()", ds.isEmpty(), true);
        outln("insertFirst(\"grape\")...");
        ds.insertFirst("grape");
        checkPeekAll(ds, "grape");
    }

    private void testInsertFirstSeveral() {
        outln(" - insertFirst() several times -");
        DSList<String> ds = createDS();
        outln("insertFirst(\"apple\")...");
        ds.insertFirst("apple");
        outln("insertFirst(\"banana\")...");
        ds.insertFirst("banana");
        outln("insertFirst(\"cherry\")...");
        ds.insertFirst("cherry");
        checkPeekAll(ds, "cherry", "banana", "apple");
    }
}
