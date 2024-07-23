package com.abc.ds.set.tests;

import com.abc.ds.set.*;

/* deliberate package access */
class TestDSSetFactoryCheck extends TestDSSetBase {
    public TestDSSetFactoryCheck(DSSetFactory factory) {
        super("factory check", factory);
    }

    @Override
    protected void performTests() {
        testCreate();
        testCreateWithCapacity();
    }

    private void testCreate() {
        outln("Creating a new DSSet with " +
            "factory.create(String.class)...");
        DSSet<String> set = factory.create(String.class);
        outln("set != null", set != null, true);
    }

    private void testCreateWithCapacity() {
        outln("Creating a new DSSet with " +
            "factory.create(String.class, 2000, 10)...");
        DSSet<String> set = factory.create(String.class, 2000, 10);
        outln("set != null", set != null, true);
    }
}
