package com.abc.ds.list.tests;

import com.abc.ds.list.*;

/* deliberate package access */
class TestDSListIndexOf extends TestDSListBase {
    public TestDSListIndexOf(DSListFactory factory) {
        super("firstIndexOf, lastIndexOf", factory);
    }

    @Override
    protected void performTests() {
        testOnEmpty();
    }

    private void testOnEmpty() {
        outln(" - testing indexOf's on empty -");
        DSList<String> ds = createDS();
        outln("ds.firstIndexOf(\"apple\", 0)", ds.firstIndexOf("apple", 0), DSList.NOT_FOUND_INDEX);
        outln("ds.firstIndexOf(\"apple\")", ds.firstIndexOf("apple"), DSList.NOT_FOUND_INDEX);
        outln("ds.lastIndexOf(\"apple\", 0)", ds.lastIndexOf("apple", 0), DSList.NOT_FOUND_INDEX);
        outln("ds.lastIndexOf(\"apple\")", ds.lastIndexOf("apple"), DSList.NOT_FOUND_INDEX);
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
