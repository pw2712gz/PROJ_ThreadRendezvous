package com.abc.ds.hash.tests;

import com.abc.ds.hash.*;

/* deliberate package access */
class TestDSHashTableBasicEmptyCount extends TestDSHashTableBase {
    public TestDSHashTableBasicEmptyCount(DSHashTableFactory factory) {
        super("empty and count tests", factory);
    }

    @Override
    protected void performTests() {
        testInitialState();
        testAddJustOne();
        testAddTwo();
        testAddFive();
    }

    private void testInitialState() {
        DSHashTable<String, String> ht = createDS();
        outln("initial state: ");
        outln("checking isEmpty()", ht.isEmpty(), true);
        outln("checking count()", ht.count(), 0);
    }

    private void testAddJustOne() {
        outln(" - add just one -");
        DSHashTable<String, String> ht = createDS();
        insert(ht, getFruitPairs(1));
        outln("checking isEmpty()", ht.isEmpty(), false);
        outln("checking count()", ht.count(), 1);
    }

    private void testAddTwo() {
        outln(" - add two -");
        DSHashTable<String, String> ht = createDS();
        insert(ht, getFruitPairs(2));
        outln("checking isEmpty()", ht.isEmpty(), false);
        outln("checking count()", ht.count(), 2);
    }

    private void testAddFive() {
        outln(" - add five -");
        DSHashTable<String, String> ht = createDS();
        insert(ht, getFruitPairs(5));
        outln("checking isEmpty()", ht.isEmpty(), false);
        outln("checking count()", ht.count(), 5);
    }
}
