package com.abc.ds.hash.tests;

import java.util.*;

import com.abc.ds.hash.*;
import com.abc.ds.keyvalue.*;
import com.abc.ds.keyvalue.tests.*;
import com.abc.ds.tests.*;

/* deliberate package access */
class TestDSHashTablePeekAll extends TestDSHashTableBase {
    public TestDSHashTablePeekAll(DSHashTableFactory factory) {
        super("peekAll()", factory);
    }

    @Override
    protected void performTests() {
        testPeekAllOnEmpty();
        testPeekAllOnOne();
        testPeekAllOnTwo();
        testPeekAllOnSeveral();
    }

    private void testPeekAllOnEmpty() {
        outln(" - peekAll() on empty -");
        DSHashTable<String, String> ds = createDS();
        checkIsEmpty(ds, true);
        checkPeekAllOnEmpty(ds);
    }

    @SuppressWarnings("unchecked")
    private void testPeekAllOnOne() {
        outln(" - peekAll() on one -");
        DSHashTable<String, String> ds = createDS();
        insert(ds, PAIR_VOLCANO);
        checkPeekAll(ds, PAIR_VOLCANO);
    }

    @SuppressWarnings("unchecked")
    private void testPeekAllOnTwo() {
        outln(" - peekAll() on two -");
        DSHashTable<String, String> ds = createDS();
        insert(ds, PAIR_OCEAN, PAIR_GLACIER);
        checkPeekAll(ds, PAIR_GLACIER, PAIR_OCEAN); // unordered
    }

    private void testPeekAllOnSeveral() {
        outln(" - peekAll() on several -");
        DSHashTable<String, String> ds = createDS();
        outln("adding some junk to be cleared before peekAll() test...");
        insert(ds, "JUNK A", "trash A");
        insert(ds, "JUNK B", "trash B");
        insert(ds, "JUNK C", "trash C");
        outln("clear()...");
        ds.clear();

        DSKeyValuePair<String, String>[] fruits = new TestFruitPairGenerator(
            TestFruitGenerator.RANDOM_SEED_5).nextRandom(20);

        insert(ds, fruits);

        DSKeyValuePair<String, String>[] expectedFruits =
            shuffle(removeDuplicateKeys(fruits), new Random(0x00000000feedface));

        checkPeekAll(ds, expectedFruits);
    }
}
