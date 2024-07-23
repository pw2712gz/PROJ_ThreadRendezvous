package com.abc.ds.hash.tests;

import com.abc.ds.hash.*;

/* deliberate package access */
class TestDSHashTableClear extends TestDSHashTableBase {
    public TestDSHashTableClear(DSHashTableFactory factory) {
        super("clear tests", factory);
    }

    @Override
    protected void performTests() {
        testAddThenClear();
        testAddThenClearThenAdd();
    }

    private void testAddThenClear() {
        outln(" - add, then clear -");
        DSHashTable<String, String> ht = createDS();
        outln("adding 5:");
        insert(ht, getFruitPairs(5));
        outln("checking count()", ht.count(), 5);
        outln("clear()");
        ht.clear();
        outln("checking isEmpty()", ht.isEmpty(), true);
        outln("checking count()", ht.count(), 0);
    }

    private void testAddThenClearThenAdd() {
        outln(" - add, then clear, then add -");
        DSHashTable<String, String> ht = createDS(7);
        outln("adding 20:");
        insert(ht, getFruitPairs(20));
        outln("checking count()", ht.count(), 20);

        outln("clear()");
        ht.clear();
        outln("checking count()", ht.count(), 0);
        outln("checking isEmpty()", ht.isEmpty(), true);

        outln("adding 6:");
        insert(ht, getFruitPairs(20, 6));
        outln("checking count()", ht.count(), 6);
        outln("checking isEmpty()", ht.isEmpty(), false);
    }
}
