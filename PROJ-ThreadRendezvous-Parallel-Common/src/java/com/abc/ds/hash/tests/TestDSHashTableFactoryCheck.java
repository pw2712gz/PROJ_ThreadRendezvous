package com.abc.ds.hash.tests;

import com.abc.ds.hash.*;

/* deliberate package access */
class TestDSHashTableFactoryCheck extends TestDSHashTableBase {
    public TestDSHashTableFactoryCheck(DSHashTableFactory factory) {
        super("factory check", factory);
    }

    @Override
    protected void performTests() {
        testCreate();
    }

    private void testCreate() {
        outln("Creating a new DSHashTable with " +
            "factory.create(String.class, String.class, 100, 0.7)...");
        DSHashTable<String, String> hashTable =
            factory.create(String.class, String.class, 100, 0.7);
        outln("hashTable != null", hashTable != null, true);
    }
}
